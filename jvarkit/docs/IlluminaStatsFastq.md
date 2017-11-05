# IlluminaStatsFastq

Reads filenames from stdin: Count FASTQs in Illumina Result.


## Usage

```
Usage: ilmnfastqstats [options] Files
  Options:
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -o, --output
      Output zip file.
    --version
      print version and exit
    -X
      maximum number of DNA indexes to print. memory consuming if not 0.
      Default: 0

```

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
$ make ilmnfastqstats
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/misc/IlluminaStatsFastq.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/misc/IlluminaStatsFastq.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **ilmnfastqstats** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

 
## Output
the software generates a directory or a zip file.

it contains the following files:

* names.tsv : file path with sample, index, lane, side , split, file size
* counts.tsv:  file path,total reads, read_fail_filter, read_do_not_fail_filter
* histopos2qual:  file path, position, mean-qual, count bases (excluding read_fail_filter)
* histquals: file path, [quality[, count reads
* lengths: file path, length, count reads (excluding read_fail_filter)
* notfastq: problem with that fastq
* quals : file, mean-quality (excluding read_fail_filter)
* bases! file, A,T,G,C,N (excluding read_fail_filter)
* indexes most frequent indexes

## Example

``` bash
$ find dir1 dir2 -type f -name "*.fastq.gz" |\
   grep -v SAMPLE1234 |\
   java -jar dist/ilmnfastqstats.jar \
   O=OUTDIR

$ ls JETER 
bases.tsv
counts.tsv
histpos2qual.tsv
histquals.tsv
lengths.tsv
names.tsv
notfastq.tsv
quals.tsv

$ find dir1 dir2 -type f -name "*.fastq.gz" |\
   grep -v SAMPLE1234 |\
   java -jar dist/ilmnfastqstats.jar \
   O=OUTDIR.zip


$ unzip -t OUTDIR.zip 
Archive:  OUTDIR.zip
    testing: names.tsv                OK
    testing: counts.tsv               OK
    testing: quals.tsv                OK
    testing: notfastq.tsv             OK
    testing: histquals.tsv            OK
    testing: histpos2qual.tsv         OK
    testing: bases.tsv                OK
    testing: lengths.tsv              OK
```

 

