# FastqRevComp

produces a reverse-complement fastq (for mate pair alignment see http://seqanswers.com/forums/showthread.php?t=5085 )


## Usage

```
Usage: fastqrevcomp [options] Files
  Options:
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -o, --output
      Output file. Optional . Default: stdout
    --version
      print version and exit
    -1
       interleaced input : only reverse complement R1
      Default: false
    -2
       interleaced input : only reverse complement R2
      Default: false

```


## Keywords

 * fastq


## Compilation

### Requirements / Dependencies

* java compiler SDK 1.8 http://www.oracle.com/technetwork/java/index.html (**NOT the old java 1.7 or 1.6**) . Please check that this java is in the `${PATH}`. Setting JAVA_HOME is not enough : (e.g: https://github.com/lindenb/jvarkit/issues/23 )
* GNU Make >= 3.81
* curl/wget
* git
* xsltproc http://xmlsoft.org/XSLT/xsltproc2.html (tested with "libxml 20706, libxslt 10126 and libexslt 815")


### Download and Compile

```bash
$ git clone "https://github.com/lindenb/jvarkit.git"
$ cd jvarkit
$ make fastqrevcomp
```

The *.jar libraries are not included in the main jar file, so you shouldn't move them (https://github.com/lindenb/jvarkit/issues/15#issuecomment-140099011 ).
The required libraries will be downloaded and installed in the `dist` directory.

### edit 'local.mk' (optional)

The a file **local.mk** can be created edited to override/add some definitions.

For example it can be used to set the HTTP proxy:

```
http.proxy.host=your.host.com
http.proxy.port=124567
```
## Source code 

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/misc/FastqRevComp.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/misc/FastqRevComp.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **fastqrevcomp** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

 
## Example


without revcomp
```bash
$ gunzip -c mate.fastq.gz | head -n 8

@M00785:3:000000000-A60G6:1:1101:15339:1356 1:N:0:1
TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTCTTTTTTCTTTCTTTTCTTTTTTTTTTTTTTCTTTTTTTTTTTTTTCTTTTTTTTTTTTCTTTTTTTTTTTTTTTTTTTCTTTTTTTTCTTTCTTTCTTTTTTTTTTCTCTCTCTTTTTTTTTCTTTCTTTTTTTCCTTTTTCCTTTTTCTTTCTTCTTTTTTCTTCTTTTTTTTCTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTATTTT
+
111>>>11>000//A//A>>>/>/>><//<<<//<-<@--/00000-/00:0::000000:9---------//9///--9------//9/99-9----9//////-----9-----9--///9/99-;/99//////99/;/-99--/////////9;9---9//9///;;//-9////////////9///://99//9///-//9/999//99-/9/;:/9--;---9-9---@--------;---//9/
@M00785:3:000000000-A60G6:1:1101:15206:1568 1:N:0:1
GCTGGTGTTCCTCAGCCACGGGGGTAGGGAACAGGCGTTACCACTTACATTCCCAGGACACCATGGCCTGTCTCTTATACACATCTAGATGTGTATAAGAGACAGGGTAACTACAATGGACCCCTTGCAGCCTGGAAGGGCCAGCAGTTCACTTTTCCAAGAGCAGCCGTGCATTCTGCACCTGAGTGTTGGCCTCTCCTGGCCATAGAGATCGGAAGAGCACACGTCTGAACTCCAGTCACCGATGTATC
+
11>11BFAA@@DFB1BB1FECE?CE//AC0CFHFGFCGGGGF1BFHGHFHHHFHFHGEFBGFHFBCFGHGHFGEFGBGFGFHFHHHFE2F2F1FFHFFFHGEGB0AEHHEHHHFFFGBFFGHGGGHHFFHFFG0/0CHGGGGGHGHHHGHGHHHHHHHHHHGHGHGHHGGCGHHHHHHHHHHHHHHGHHHAEHGGGGGGGGFGGGGFGFFGGGGBBEGEGGGFFF?FEFFFFFFFFFFFFFFFF?@FFFFB
```
with revcomp
```bash
 gunzip -c mate.fastq.gz | head -n 8 | java -jar dist/fastqrevcomp.jar

@M00785:3:000000000-A60G6:1:1101:15339:1356 1:N:0:1
AAAATAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGAAAAAAAAGAAGAAAAAAGAAGAAAGAAAAAGGAAAAAGGAAAAAAAGAAAGAAAAAAAAAGAGAGAGAAAAAAAAAAGAAAGAAAGAAAAAAAAGAAAAAAAAAAAAAAAAAAAGAAAAAAAAAAAAGAAAAAAAAAAAAAAGAAAAAAAAAAAAAAGAAAAGAAAGAAAAAAGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
+
/9//---;--------@---9-9---;--9/:;/9/-99//999/9//-///9//99//:///9////////////9-//;;///9//9---9;9/////////--99-/;/99//////99/;-99/9///--9-----9-----//////9----9-99/9//------9--///9//---------9:000000::0:00/-00000/--@<-<//<<<//<>>/>/>>>A//A//000>11>>>111
@M00785:3:000000000-A60G6:1:1101:15206:1568 1:N:0:1
GATACATCGGTGACTGGAGTTCAGACGTGTGCTCTTCCGATCTCTATGGCCAGGAGAGGCCAACACTCAGGTGCAGAATGCACGGCTGCTCTTGGAAAAGTGAACTGCTGGCCCTTCCAGGCTGCAAGGGGTCCATTGTAGTTACCCTGTCTCTTATACACATCTAGATGTGTATAAGAGACAGGCCATGGTGTCCTGGGAATGTAAGTGGTAACGCCTGTTCCCTACCCCCGTGGCTGAGGAACACCAGC
+
BFFFF@?FFFFFFFFFFFFFFFFEF?FFFGGGEGEBBGGGGFFGFGGGGFGGGGGGGGHEAHHHGHHHHHHHHHHHHHHGCGGHHGHGHGHHHHHHHHHHGHGHHHGHGGGGGHC0/0GFFHFFHHGGGHGFFBGFFFHHHEHHEA0BGEGHFFFHFF1F2F2EFHHHFHFGFGBGFEGFHGHGFCBFHFGBFEGHFHFHHHFHGHFB1FGGGGCFGFHFC0CA//EC?ECEF1BB1BFD@@AAFB11>11
```


