# CaseControlJfx

display jfx chart of case/control maf from a VCF and a pedigree


## Usage

```
Usage: casectrljfx [options] Files
  Options:
    -filter, --filter
      Ignore FILTERed variants
      Default: false
    -gtfilter, --genotypefilter
      Ignore FILTERed Genotypes
      Default: false
    -h, --help
      print help and exit
    --helpFormat
      What kind of help
      Possible Values: [usage, markdown, xml]
    --legendside
      Legend side
      Default: RIGHT
      Possible Values: [TOP, BOTTOM, LEFT, RIGHT]
    --limit
      Limit to 'N' variants. negative==no limit; All point are loaded in 
      memory. The more variants you have, the more your need memory
      Default: -1
    -mafTag, --mafTag
      Do not calculate MAF for controls, but use this tag to get Controls' MAF
    -nchr, --nocallishomref
      treat no call as HomRef
      Default: false
    --opacity
      Point opaciy [0-1]
      Default: 0.4
    -o, --out
      Save the image in a file and then exit.
    -partition, --partition
      partition type. How series are built. For example 'variantType' will 
      produces some series for INDEL, SNP, etc...
      Default: variantType
      Possible Values: [chromosome, variantType, autosomes, qual, vqslod, typeFilter, distance, n_alts]
    -p, --ped, --pedigree
      Pedigree File. If not defined, try to use the pedigree inserted in the 
      VCF header.
    --sex
      Select/Filter samples on their gender.
      Default: all
      Possible Values: [all, males, females]
    --title
      Default title for the graph
    --tooltip
      add mouse Tooltip the point (requires more memory)
      Default: false
    --version
      print version and exit

```


## Keywords

 * vcf
 * pedigree
 * case
 * control
 * visualization
 * jfx
 * chart
 * maf


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
$ make casectrljfx
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

[https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/burden/CaseControlJfx.java
](https://github.com/lindenb/jvarkit/tree/master/src/main/java/com/github/lindenb/jvarkit/tools/burden/CaseControlJfx.java
)
## Contribute

- Issue Tracker: [http://github.com/lindenb/jvarkit/issues](http://github.com/lindenb/jvarkit/issues)
- Source Code: [http://github.com/lindenb/jvarkit](http://github.com/lindenb/jvarkit)

## License

The project is licensed under the MIT license.

## Citing

Should you cite **casectrljfx** ? [https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md](https://github.com/mr-c/shouldacite/blob/master/should-I-cite-this-software.md)

The current reference is:

[http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)

> Lindenbaum, Pierre (2015): JVarkit: java-based utilities for Bioinformatics. figshare.
> [http://dx.doi.org/10.6084/m9.figshare.1425030](http://dx.doi.org/10.6084/m9.figshare.1425030)


### Example

```
java -jar dist/casectrljfx.jar --pedigree  mutations.ped mutations.vcf
```
### See also:

  * https://twitter.com/yokofakun/status/860495863633805312

## screenshot

![screenshot](https://pbs.twimg.com/media/C_EYa54W0AAopkl.jpg)



