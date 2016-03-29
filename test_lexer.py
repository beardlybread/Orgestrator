import unittest
from collections import namedtuple

LEX_LINE = r"\[@\d+,\d+:\d+='(.+)',<(\d+)>,\d+:\d+\]"
test_case = namedtuple("test_case", ("input", "to_be"))

class TestLexer(unittest.TestCase):

    def setUp(self):
        self.build_lexer()
        self.run_lexer()

    def runTest(self):
        with open(output_file) as f:
            self.expect("* heading1").to_be(
                ("* ", 1),
                ("heading1\n", 7)) 
            self.expect("*** heading2").to_be(
                ("*** ", 1),
                ("heading2\n", 7))
            self.expect("*not-heading1").to_be(
                ("*not-heading1\n", 11))
            self.expect("  * not-heading2").to_be(
                ("  ", 2),
                ("* ", 4),
                ("not-heading2\n", 11))
            self.expect("|table|test|").to_be(
                ("|", 3),
                ("table", 9),
                ("|", 10),
                ("test", 9),
                ("|", 10),
                ("\n", 8))
            self.expect("|-----+----|").to_be(
                ("|", 3),
                ("-----+----", 9),
                ("|", 10),
                ("\n", 8))
            self.expect(r"|\|-4\| = 4|").to_be(
                ("|", 3),
                ("|-4| = 4", 9),
                ("|", 10),
                ("\n", 8))
            self.expect("- ulist1").to_be(
                ("- ", 4),
                ("ulist1\n", 11))
            self.expect("+ ulist2").to_be(
                ("+ ", 4),
                ("ulist2\n", 11))
            self.expect("-100 (not-ulist1)").to_be(
                ("-100 (not-ulist1)\n", 11))
            self.expect("-- not-ulist2").to_be(
                ("-- not-ulist2\n", 11))
            self.expect("1. ilist1").to_be(
                ("1. ", 5),
                ("ilist1\n", 11))
            self.expect("1.not-ilist1").to_be(
                ("1.not-ilist1\n", 11))
            self.expect("2 not-ilist2").to_be(
                ("2 not-ilist2\n", 11))
            self.expect("\r\n").to_be(
                ("\r\n", 
            


        #    | t2 | t2 |

        #  | \|t3 |

        #  - ulist1

        #  + ulist2

        #    - ulist3

        #    * ulist4

        #  -not-ulist1

        #  ++ not-ulist2

        #  * not-ulist3

        #  1. ilist1

        #    2. ilist2

        #  1.not-ilist1

        #    2 not-ilist2

        #  something-else1

        #      something-else2

        #  <newline>

        #  <windows-newline>


