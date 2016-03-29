import os
import re
import subprocess
import unittest
from collections import namedtuple

LEX_TOKEN = r"\[@\d+,\d+:\d+='(.+)',<(-?\d+)>,\d+:\d+\]"

class TestLexer(unittest.TestCase):

    def runTest(self):
        self.expect("* heading1").to_be(
            ("* ", 1),
            ("heading1\\n", 7)) 
        self.expect("*** heading2").to_be(
            ("*** ", 1),
            ("heading2\\n", 7))
        self.expect("*not-heading1").to_be(
            ("*not-heading1\\n", 11))
        self.expect("  * not-heading2").to_be(
            ("  ", 2),
            ("* ", 4),
            ("not-heading2\\n", 11))
        self.expect("|table|test|").to_be(
            ("|", 3),
            ("table", 9),
            ("|", 10),
            ("test", 9),
            ("|", 10),
            ("\\n", 8))
        self.expect("|-----+----|").to_be(
            ("|", 3),
            ("-----+----", 9),
            ("|", 10),
            ("\\n", 8))
        self.expect(r"|\|-4\| = 4|").to_be(
            ("|", 3),
            (r"\|-4\| = 4", 9),
            ("|", 10),
            ("\\n", 8))
        self.expect("- ulist1").to_be(
            ("- ", 4),
            ("ulist1\\n", 11))
        self.expect("+ ulist2").to_be(
            ("+ ", 4),
            ("ulist2\\n", 11))
        self.expect("-100 (not-ulist1)").to_be(
            ("-100 (not-ulist1)\\n", 11))
        self.expect("-- not-ulist2").to_be(
            ("-- not-ulist2\\n", 11))
        self.expect("1. ilist1").to_be(
            ("1. ", 5),
            ("ilist1\\n", 11))
        self.expect("1.not-ilist1").to_be(
            ("1.not-ilist1\\n", 11))
        self.expect("2 not-ilist2").to_be(
            ("2 not-ilist2\\n", 11))
        self.expect("\r\n").to_be(
            (r"\r\n", 6),
            ("\n", 6))
            
    def expect(self, testString):

        def parse_token(tok):
            m = re.search(LEX_TOKEN, tok)
            return (m.group(1), int(m.group(2))) if m else (None, None)

        def to_be(*tokens):
            resp = subprocess.Popen(
                ["java", "org.antlr.v4.runtime.misc.TestRig", "OrgMode",
                 "tokens", "-tokens"],
                stdin=subprocess.PIPE,
                stdout=subprocess.PIPE).communicate(testString + "\n")
            response = [parse_token(t) for t in resp[0].strip().split("\n")][:-1]
            for tok, resp in zip(tokens, response):
                self.assertEqual(tok, resp, msg='%s != %s, for "%s"'
                                                '' % (tok, resp, testString))

        return namedtuple("Expect", ("test", "to_be"))(testString, to_be)

    def setUp(self):
        os.system("java -Xmx500M org.antlr.v4.Tool OrgMode.g4")
        os.system("javac OrgMode.java")

    def tearDown(self):
        os.remove("OrgMode.java")
        os.remove("OrgMode.tokens")
        os.remove("OrgMode.class")

        
if __name__ == "__main__":
    unittest.main()

