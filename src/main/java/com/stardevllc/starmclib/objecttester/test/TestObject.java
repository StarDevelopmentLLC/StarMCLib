package com.stardevllc.starmclib.objecttester.test;

import java.util.Random;

public class TestObject {
    private static final Random RANDOM = new Random();
    
    private static final int testConstantInt = RANDOM.nextInt(1001) + 1000;
    private static final double testConstantDouble = RANDOM.nextDouble(1000) + 1000;
    private static final String testConstantString = "This is a Constant String";
    private static final char testConstantChar = 'c';
    
    private static int testStaticInt = RANDOM.nextInt(1000) + 100;
    private static double testStaticDouble = RANDOM.nextDouble(1000) + 100;
    private static String testStaticString = "This is a static String";
    private static char testStaticChar = 's';
    
    private final int testFinalInt = RANDOM.nextInt(101);
    private final double testFinalDouble = RANDOM.nextDouble(101);
    private final String testFinalString = "This is a final instance String";
    private final char testFinalChar = 'f';
    
    private int testInt = RANDOM.nextInt(11);
    private double testDouble = RANDOM.nextDouble(11);
    private String testString = "This is an instance String";
    private char testChar = 'i';

    public TestObject() {
        
    }
    
    public TestObject(int testInt) {
        this.testInt = testInt;
    }

    public TestObject(int testInt, double testDouble) {
        this.testInt = testInt;
        this.testDouble = testDouble;
    }

    public TestObject(int testInt, double testDouble, String testString) {
        this.testInt = testInt;
        this.testDouble = testDouble;
        this.testString = testString;
    }

    public TestObject(int testInt, double testDouble, String testString, char testChar) {
        this.testInt = testInt;
        this.testDouble = testDouble;
        this.testString = testString;
        this.testChar = testChar;
    }
    
    public void testMethod() {
        this.testInt += this.testInt;
    }
    
    public void testMethod(int value) {
        this.testInt += value;
    }
    
    public void testMethod(int value, int other) {
        this.testInt += value;
        this.testInt -= value;
    }
    
    public int otherTest() {
        this.testInt += 9;
        return this.testInt;
    }
    
    private void privateTest() {
        this.testDouble += this.testDouble;
    }
}
