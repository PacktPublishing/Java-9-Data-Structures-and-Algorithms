package com.example.datastructure;

/**
 * Created by debasish on 24/7/16.
 */
public class DecimalInteger implements Cloneable {
    char[] digits;
    Character optionalCarryDigit;
    int shift;

    protected DecimalInteger() {

    }

    public DecimalInteger(String value) {
        digits = value.toCharArray();
        //Check whether digits are okay
        for (char digit : digits) {
            if (digit > '9' || digit < '0') {
                throw new NumberFormatException("Illegal character '" + digit + "'");
            }
        }
    }

    public DecimalInteger(DecimalInteger source) {
        if (source.optionalCarryDigit != null) {
            digits = new char[source.digits.length + 1];
            digits[0] = source.optionalCarryDigit;
            System.arraycopy(source.digits, 0, digits, 1, source.digits.length);
        } else {
            digits = source.digits;
        }
        shift = source.shift;
    }

    @Override
    public DecimalInteger clone() {
        return new DecimalInteger(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (optionalCarryDigit == null) {
            sb.append(digits);
        } else {
            sb.append(clone().toString());
        }
        for (int i = 0; i < shift; i++) {
            sb.append('0');
        }
        return sb.toString();
    }

    public DecimalInteger add(DecimalInteger rhs) {
        int resultShift = Math.min(shift, rhs.shift);
        int minResultDigs = Math.max(digits.length + shift, rhs.digits.length + rhs.shift)
                - resultShift;

        if (optionalCarryDigit != null || rhs.optionalCarryDigit != null) {
            minResultDigs++;
        }

        char[] resultDigs = new char[minResultDigs];
        int currentCarry = 0;
        int lhsPos = digits.length - 1 + shift - resultShift;
        int rhsPos = rhs.digits.length - 1 + rhs.shift - resultShift;
        for (int resultPos = minResultDigs - 1; resultPos >= 0; resultPos--) {
            int resultValueForDig = 0;
            if (lhsPos < digits.length && lhsPos >= 0)
                resultValueForDig += (digits[lhsPos] - '0');
            else if (lhsPos == -1 && optionalCarryDigit != null) {
                resultValueForDig += 1;
            }

            if (rhsPos < rhs.digits.length && rhsPos >= 0)
                resultValueForDig += (rhs.digits[rhsPos] - '0');
            else if (rhsPos == -1 && rhs.optionalCarryDigit != null) {
                resultValueForDig += 1;
            }
            resultValueForDig += currentCarry;
            if (resultValueForDig <= 9) {
                resultDigs[resultPos] = (char) (resultValueForDig + '0');
                currentCarry = 0;
            } else {
                resultDigs[resultPos] = (char) (resultValueForDig - 10 + '0');
                currentCarry = 1;
            }
            lhsPos--;
            rhsPos--;
        }
        DecimalInteger result = new DecimalInteger();
        result.digits = resultDigs;
        if (currentCarry > 0) {
            result.optionalCarryDigit = (char) (currentCarry + '0');
        }
        result.shift = resultShift;

        return result;

    }

    protected DecimalInteger shiftBy(int places){
        DecimalInteger newOne = clone();
        newOne.shift+=places;
        return newOne;
    }

    protected DecimalInteger multiplyByADigit(char digit) {
        int minResultDigs = digits.length;
        if (optionalCarryDigit != null) {
            minResultDigs++;
        }
        char[] resultDigs = new char[minResultDigs];
        int currentCarry = 0;
        int multiplier = digit - '0';
        int thisPosition = digits.length - 1;
        for (int i = minResultDigs - 1; i >= 0; i--) {
            int resultDig = currentCarry;
            if (thisPosition >= 0)
                resultDig += (digits[thisPosition] - '0') * multiplier;
            currentCarry = resultDig / 10;
            resultDig = resultDig % 10;
            resultDigs[i] = (char) (resultDig + '0');
            thisPosition--;
        }
        DecimalInteger decimalInteger = new DecimalInteger();
        if (currentCarry > 0) {
            decimalInteger.optionalCarryDigit = (char) (currentCarry + '0');
        }
        decimalInteger.digits = resultDigs;
        decimalInteger.shift = shift;
        return decimalInteger;
    }

    public DecimalInteger multiply(DecimalInteger rhs){
        DecimalInteger currentSum = new DecimalInteger("0");
        int resultShift = rhs.shift;
        if(rhs.optionalCarryDigit!=null){
            currentSum = multiplyByADigit(rhs.optionalCarryDigit).shiftBy(rhs.digits.length);
        }
        for(int i=rhs.digits.length -1 ;i>=0;i--){
            currentSum = currentSum.add(multiplyByADigit(rhs.digits[i]).shiftBy(rhs.digits.length -1 - i));
        }
        return currentSum.shiftBy(resultShift);

    }

    public static void main(String[] args) {
        DecimalInteger lhs = new DecimalInteger("20394");
        lhs.shift = 1;
        System.out.println(lhs);
        DecimalInteger rhs = new DecimalInteger("999999");
        rhs.shift = 3;
        DecimalInteger third = new DecimalInteger("979");
        third.shift = 1;
        System.out.println(lhs.add(rhs));
        System.out.println(lhs.add(rhs).add(third));
        System.out.println(lhs.multiplyByADigit('5'));
        System.out.println(lhs.multiply(third));
    }
}
