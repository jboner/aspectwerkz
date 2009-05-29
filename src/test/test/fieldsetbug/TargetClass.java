/**************************************************************************************
 * Copyright (c) Jonas Bonér, Alexandre Vasseur. All rights reserved.                 *
 * http://aspectwerkz.codehaus.org                                                    *
 * ---------------------------------------------------------------------------------- *
 * The software in this package is published under the terms of the LGPL license      *
 * a copy of which has been included with this distribution in the license.txt file.  *
 **************************************************************************************/
package test.fieldsetbug;


public class TargetClass {

    public int publicIntField;
    public char publicCharField;
    public long publicLongField;
    public double publicDoubleField;

    public TargetClass() {
        publicIntField    = 1;
        publicCharField   = 'a';
        publicLongField   = 1L;
        publicDoubleField = 1D;
    }

    public TargetClass(int value) {
        publicIntField = value;
    }

    public TargetClass(char value) {
        publicCharField = value;
    }
	
	public TargetClass(long value) {
		publicLongField = value;
	}
	
	public TargetClass(double value) {
		publicDoubleField = value;
	}
}
