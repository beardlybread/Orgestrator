package com.github.beardlybread.orgestrator.org;

import com.github.beardlybread.orgestrator.io.DriveApi;
import com.github.beardlybread.orgestrator.org.antlr.OrgLexer;
import com.github.beardlybread.orgestrator.org.antlr.OrgParser;
import com.github.beardlybread.orgestrator.util.Predicate;
import com.google.api.client.http.AbstractInputStreamContent;

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

    public void setDriveApi (DriveApi driveApi) {
        this.driveApi = driveApi;
    }

    public void loadFilesFromGoogleDrive (String[] filePaths) {
        if (filePaths != null && filePaths.length > 0) {
            DriveApi.RequestQueue downloads = this.driveApi.new RequestQueue();
            for (String filePath : filePaths)
                downloads.request(this.driveApiDownloadRequest(filePath));
            downloads.execute();
        }
    }

    public void saveFilesToGoogleDrive () {
        DriveApi.RequestQueue uploads = this.driveApi.new RequestQueue();
        for (OrgFile file: this.data) {
            if (file.getResourceType() == OrgFile.GOOGLE_DRIVE_RESOURCE)
                try {
                    uploads.request(this.driveApiUploadRequest(file));
                } catch (IOException e) {
                    driveApi.makeToast(
                            "File upload failed:" + file.getResourcePath().split("\t")[0]);
                }
        }
        uploads.execute();
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
                driveApi.makeToast("File downloaded: " + name);
            }
        };
    }

    public DriveApi.Request driveApiUploadRequest (OrgFile file) throws IOException {
        String[] nameAndId = file.getResourcePath().split("\t");
        final String name = nameAndId[0], id = nameAndId[1];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        file.write(out);
        byte[] data = out.toByteArray();
        final ByteArrayInputStream content = new ByteArrayInputStream(data);
        final long length = data.length;
        return this.driveApi.new Request() {
            @Override
            public byte[] call(DriveApi.MakeRequest makeRequest) throws IOException {
                makeRequest.getService().files().update(id, null,
                        new AbstractInputStreamContent(null) {
                            @Override
                            public InputStream getInputStream() throws IOException {
                                return content;
                            }

                            @Override
                            public long getLength() throws IOException {
                                return length;
                            }

                            @Override
                            public boolean retrySupported() {
                                return false;
                            }
                        }).execute();
                return null;
            }

            @Override
            public void after (DriveApi.MakeRequest makeRequest, byte[] output) {
                driveApi.makeToast("File saved: " + name);
            }
        };
    }
}
