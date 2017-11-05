/*
The MIT License (MIT)

Copyright (c) 2017 Pierre Lindenbaum

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
package com.github.lindenb.jvarkit.tools.vcfviewgui.chart;

import java.util.Arrays;

import com.github.lindenb.jvarkit.util.Counter;

import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMUtils;
import htsjdk.samtools.fastq.FastqRecord;
import htsjdk.samtools.util.SequenceUtil;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ReadQualityChartFactory extends ReadChartFactory
	{
    /** Quality count */
    private final Counter<Integer> quality2count=new Counter<>();
    
    @Override
    public String getName() {
    	return "Mean Read Quality";
    	}
    
    @Override
    public void visit(final SAMRecord rec) {
    	byte quals[]= rec.getBaseQualities();
    	if(quals==null || quals.length==0) return ;
    	
    	if(! rec.getReadUnmappedFlag() && rec.getReadNegativeStrandFlag())
    		{
    		quals = Arrays.copyOf(quals, quals.length);//because it would modify the read itself.
    		SequenceUtil.reverseQualities(quals);
    		}
        _visit(quals);	
        }
    
    @Override
    public void visit(final FastqRecord rec) {
    	final byte quals[]= rec.getBaseQualityString().getBytes();
    	if(quals==null || quals.length==0) return;
    	SAMUtils.fastqToPhred(quals);
        _visit(quals);	
        }
    private void _visit(final byte quals[]) {
        if(quals==null || quals.length==0) return;
        double meanqual=0.0;
    	for(int x=0;x< quals.length;++x)
    		{
    		meanqual+=quals[x];
    		}
    	this.quality2count.incr((int)(100.0*(meanqual)/quals.length));
    	}
    
    @Override
    public LineChart<Number, Number> build()
        {
    	final NumberAxis xAxis = new NumberAxis();
    	xAxis.setLabel("Quality");
        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");

    	final  XYChart.Series<Number,Number> serie = new XYChart.Series<Number,Number>();
    	serie.setName(xAxis.getLabel());
    	
        for(int g=0;g<=100;++g)
        	{
        	serie.getData().add(new XYChart.Data<Number,Number>(
        			g,this.quality2count.count(g))
        			);
        	}
        
        
        final LineChart<Number, Number> sbc =
                new LineChart<Number, Number>(xAxis, yAxis);
        sbc.setTitle(this.getName());
        sbc.getData().add(serie);
        sbc.setCreateSymbols(false);
        sbc.setLegendVisible(false);
        return sbc;
        }
    
}
