# VCFTrios

Find mendelian incompatibilitie in a VCF


## Usage

```
Usage: vcftrio [options] Files
  Options:
    -A, --attribute
      INFO Attribute name
      Default: MENDEL
    --discard
      Discard variants without mendelian incompatibilities
      Default: false
    -f, --filter
      filter name. create a filter in the FILTER column
    -gf, --gfilter
      genotype filter name. create a filter in the GENOTYPE column
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    -if, --inversefilter
      inverse FILTER, flag variant having NO mendelian incompat.
      Default: false
    -o, --output
      Output file. Optional . Default: stdout
  * -p, --ped, --pedigree
      Pedigree file. A pedigree is a text file delimited with tabs. No header. 
      Columns are (1) Family (2) Individual-ID (3) Father Id or '0' (4) Mother 
      Id or '0' (5) Sex : 1 male/2 female / 0 unknown (6) Status : 0 
      unaffected, 1 affected,-9 unknown
    --version
      print version and exit

```


## Keywords

 * vcf
 * mendelian
 * pedigree


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
$ make vcftrio
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/vcftrios/VCFTrios.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/vcftrios/VCFTrios.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **vcftrio** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


## Example

a pedigree file:

```
$  cat pedigree.txt 

A	SAMPLE_P	0	0	0
A	SAMPLE_M	0	0	0
A	SAMPLE_E	SAMPLE_P	SAMPLE_M	0
```


find mendelian incompatibilities:

```
$  gunzip -c input.vcf.gz |\
   java -jar dist/vcftrio.jar -p pedigree.txt | grep -E '(#CHROM|MENDEL=SAMPLE_E)' |\
   verticalize 

(...)
>>> 23
$1	#CHROM	X
$2	POS	0573
$3	ID	rs358
$4	REF	G
$5	ALT	A
$6	QUAL	85.60
$7	FILTER	PASS
$8	INFO	MENDEL=SAMPLE_E
$9	FORMAT	GT:DP:DP4:GP:GQ:PL
$10	SAMPLE_E	0/1:11:6,0,5,0:97,0,122:97:96,0,118
$11	SAMPLE_M	1/1:5:0,0,5,0:134,19,0:19:120,15,0
$12	SAMPLE_P	1/1:6:0,0,6,0:136,22,0:22:121,18,0
<<< 23
(...)
>>> 59
$1	#CHROM	Y
$2	POS	19
$3	ID	rs5678
$4	REF	CA
$5	ALT	C,CAA
$6	QUAL	31.86
$7	FILTER	PASS
$8	INFO	MENDEL=SAMPLE_E
$9	FORMAT	GT:DP:DP4:GP:GQ
$10	SAMPLE_E	2/2:80:3,0,43,34:.,.,108,.,203,0:99
$11	SAMPLE_M	.
$12	SAMPLE_P	1/1:53:0,0,27,26:81,99,0,.,.,.:81
<<< 59

```


 

