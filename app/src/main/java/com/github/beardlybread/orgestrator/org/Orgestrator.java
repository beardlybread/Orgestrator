/*
The MIT License (MIT)

Copyright (c) 2016 Bradley Steinbacher

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.io.DriveApi;
import com.github.beardlybread.orgestrator.org.antlr.OrgLexer;
import com.github.beardlybread.orgestrator.org.antlr.OrgParser;
import com.github.beardlybread.orgestrator.util.Predicate;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.model.File;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class Orgestrator {

    private Vector<OrgFile> data = null;
    private IOException err = new IOException("Unknown error");

    private Orgestrator () { this.data = new Vector<>(); }
    private static final Orgestrator theInstance = new Orgestrator();
    public static Orgestrator getInstance () { return theInstance; }

    ////////////////////////////////////////////////////////////////////////////
    // Getters
    ////////////////////////////////////////////////////////////////////////////

    public OrgFile find (String path, int type) {
        for (OrgFile file: this.data) {
            if (file.getResourcePath().equals(path) && file.getResourceType() == type) {
                return file;
            }
        }
        return null;
    }

    public IOException getError () { return this.err; }
    public OrgFile getFile (int index) { return this.data.get(index); }

    public boolean isEmpty () { return this.data.isEmpty(); }

    public List<OrgNode> search (Predicate<OrgNode> predicate) {
        List<OrgNode> out = new ArrayList<>();
        for (OrgFile f: this.data) {
            out.addAll(f.search(predicate));
        }
        return out;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Setters
    ////////////////////////////////////////////////////////////////////////////

    public boolean add (InputStream inStream, String path, int type) {
        if (this.find(path, type) == null) {
            try (InputStreamReader r = new InputStreamReader(inStream)) {
                ParseTree tree;
                OrgLexer lexer = new OrgLexer(null);
                ANTLRInputStream ais = new ANTLRInputStream(r);
                lexer.setInputStream(ais);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                tree = (new OrgParser(tokens).file());
                OrgFile file = new OrgFile(path, type);
                ParseTreeWalker walker = new ParseTreeWalker();
                walker.walk(file, tree);
                this.data.add(file);
            } catch (IOException e) {
                this.err = e;
                return false;
            }
            return true;
        }
        return false;
    }

    public void clear () { this.data.clear(); }

    public void clearError () { this.err = new IOException("Unknown error"); }

    ////////////////////////////////////////////////////////////////////////////////
    // DriveApi stuff
    ////////////////////////////////////////////////////////////////////////////////

    private DriveApi driveApi = null;
    private String driveApiFolderId = null;

    public void setDriveApi (DriveApi driveApi) {
        this.driveApi = driveApi;
    }

    public void setDriveApiFolderId (String id) {
        this.driveApiFolderId = id;
    }

    public void loadFilesFromGoogleDrive (String[] filePaths) {
        this.loadFilesFromGoogleDrive(filePaths, null);
    }

    public void loadFilesFromGoogleDrive (String[] filePaths, Runnable then) {
        if (this.driveApi != null) {
            if (filePaths != null && filePaths.length > 0) {
                DriveApi.RequestQueue downloads = this.driveApi.new RequestQueue();
                for (String filePath : filePaths) {
                    if (this.find(filePath, OrgFile.GOOGLE_DRIVE_RESOURCE) == null)
                        downloads.request(this.driveApiDownloadRequest(filePath));
                }
                downloads.request(this.driveApi.emptyRequest(then));
                downloads.execute();
            }
        }
    }

    public void saveFilesToGoogleDrive () {
        this.saveFilesToGoogleDrive(null);
    }

    public void saveFilesToGoogleDrive (Runnable then) {
        if (this.driveApi != null) {
            DriveApi.RequestQueue uploads = this.driveApi.new RequestQueue();
            for (OrgFile file : this.data) {
                if (file.getResourceType() == OrgFile.GOOGLE_DRIVE_RESOURCE)
                    try {
                        uploads.request(this.driveApiUploadRequest(file));
                    } catch (IOException e) {
                        driveApi.makeToast(
                                "File upload failed:" + file.getRawPath().split("\t")[0]);
                    }
            }
            uploads.request(this.driveApi.emptyRequest(then));
            uploads.execute();
        }
    }

    public DriveApi.Request driveApiDownloadRequest (final String filePath) {
        String[] nameAndId = filePath.split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        return this.driveApi.new Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                makeRequest.getService().files().get(id)
                        .executeMediaAndDownloadTo(out);
                return out.toByteArray();
            }

            @Override
            public void after(DriveApi.MakeRequest makeRequest, byte[] output) {
                ByteArrayInputStream input = new ByteArrayInputStream(output);
                Orgestrator.getInstance().add(input, filePath, OrgFile.GOOGLE_DRIVE_RESOURCE);
                driveApi.makeToast("File loaded: " + name);
            }
        };
    }

    public DriveApi.Request driveApiUploadRequest (final OrgFile file) throws IOException {
        String[] nameAndId = file.getRawPath().split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        file.write(out);
        final byte[] data = out.toByteArray();
        return this.driveApi.new Request() {

            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                makeRequest.getService().files().delete(id).execute();
                File meta = new File();
                meta.setName(name);
                List<String> parents = new ArrayList<>();
                parents.add(driveApiFolderId);
                meta.setParents(parents);
                File result = makeRequest.getService().files()
                        .create(meta, new ByteArrayContent("text/plain; charset=utf-8", data))
                        .execute();
                file.setRawPath(result.getName() + "\t" + result.getId());
                return null;
            }

            @Override
            public void after (DriveApi.MakeRequest makeRequest, byte[] output) {
                driveApi.makeToast("File saved: " + name);
            }
        };
    }
}
