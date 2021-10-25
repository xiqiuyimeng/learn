package org.demo.learn.util;

/**
 * @author luwt
 * @date 2020/11/17.
 */
public class BitOperations {

    // 左移规则：丢弃左边（高位）指定位数，右边（低位）补0，移位数大于整形最大位数时进行取余计算，余数为需要移动的位数，可能会导致正负转变的情况

    // 右移规则：丢弃右边（低位）指定位数，左边补上符号位，移位数大于整形最大位数时进行取余计算，余数为需要移动的位数

    // 无符号右移规则：与右移规则一样，不同之处在于，左边补0，负数移动后也会变成正数

    static void intBitOperation() {
        // 任意定义数字
        int intValue = 12345678;
        System.out.println(Integer.toString(intValue, 2));
        System.out.println(Integer.toBinaryString(intValue));
        System.out.println(Integer.toBinaryString(-1));
        // 打印值
        System.out.println("intValue: " + intValue);
        // 左移1位
        System.out.println("intValue左移1位: " + (intValue << 1));
        // 左移8位
        System.out.println("intValue左移8位: " + (intValue << 8));
        // 由于int类型在二进制中位数为32位，故当左移位数大于32，会对32先取余，余数作为移动位数
        System.out.println("intValue左移32位: " + (intValue << 32));
        // 左移40位
        System.out.println("intValue左移40位: " + (intValue << 40));

        // 右移1位
        System.out.println("intValue右移1位：" + (intValue >> 1));
        // 右移8位
        System.out.println("intValue右移8位：" + (intValue >> 8));
        // 非负数右移31位等于0
        System.out.println("intValue右移31位：" + (intValue >> 31));
        // 负数右移31位等于 -1
        System.out.println("负intValue右移31位：" + (-intValue >> 31));
        System.out.println(Integer.toBinaryString(-intValue));
        // 右移32位，无变化
        System.out.println("intValue右移32位：" + (intValue >> 32));
        // 右移40位，与左移规则一样，等价于右移8位
        System.out.println("intValue右移40位：" + (intValue >> 40));

        // 无符号右移1位
        System.out.println("intValue无符号右移1位：" + (intValue >>> 1));
        System.out.println("负intValue无符号右移31位：" + (-intValue >>> 31));
    }

    static void longBitOperation() {
        // 定义数字
        long longValue = 12345678L;
        // 与int类型相似，当左移位数大于64时取余
        System.out.println("longValue: " + longValue);
        // 左移1位
        System.out.println("longValue左移1位：" + (longValue << 1));
        // 左移8位
        System.out.println("longValue左移8位：" + (longValue << 8));
        // 左移64位
        System.out.println("longValue左移64位：" + (longValue << 64));
        // 左移72位
        System.out.println("longValue左移72位：" + (longValue << 72));
    }

    /**
     * short、byte、char在做移位之前会自动转换为int型，因此与int规则一致。
     * 在做完移位运算后，short、byte、char类型都将变成int型数据。
     */
    static void otherBitOperation() {
        // 定义数字
        short shortValue = 12345;
        System.out.println("shortValue：" + shortValue);
        // 左移1位
        int shortAfterBitOperation = shortValue << 1;
        System.out.println("shortValue左移1位：" + shortAfterBitOperation);
        // 左移32位，取余
        System.out.println("shortValue左移16位：" + (shortValue << 32));
        // 定义byte数据
        byte byteValue = 1;
        System.out.println("byteValue: " + byteValue);
        // 左移1位
        int byteAfterBitOperation = byteValue << 1;
        System.out.println("byteValue左移1位：" + byteAfterBitOperation);
        // 左移32位
        System.out.println("byteValue左移32位：" + (byteValue << 32));
        // 定义char，字母a
        char charValue = 97;
        System.out.println("charValue: " + charValue);
        // 左移1位
        int charAfterBitOperation = charValue << 1;
        System.out.println("charValue左移1位：" + charAfterBitOperation);
        // 左移32位
        System.out.println("charValue左移32位：" + (charValue << 32));
    }

    static void convert() {
        // int类型128的原码：0000 0000 0000 0000 0000 0000 1000 0000，反码和补码相同
        // 当int被强转为byte类型时，由于byte只有1个字节，截断高24位，保留低8位：1000 0000
        // 在进行打印时，short、byte类型都会作为int类型计算，此时会进行有符号填充操作，前24位都填1
        // 1111 1111 1111 1111 1111 1111 1000 0000，对补码 -1 再取反：
        // 1111 1111 1111 1111 1111 1111 0111 1111
        // 1000 0000 0000 0000 0000 0000 1000 0000，得到最终结果，由于除符号位以外，
        // 高位都是0，有效位为8位，128，加上标志位，结果是-128
        int intValue = 128;
        byte byteValue = (byte) intValue;
        System.out.println(byteValue);
    }


    static void judgeTheSignOfNumber(int num) {
        // 判断正负：如果是正数，右移31位，应该等于0，负数右移后等于-1，0与正数结果一致，不考虑
        int judge = num >> 31;
        String str = judge == 0 ? "正数" : "负数";
        System.out.println(str);
    }

    static void isPowerOf2(int num) {
        // 判断一个数是否是2的幂，如果是2的幂，应满足：正整数，二进制中只存在一个1。
        // 所以若是2的幂，当num - 1后，原最高位变为0，右侧都是1，与原二进制进行与运算应该等于0
        int result = num & (num - 1);
        String msg = result == 0 ? "num是2的幂": "num不是2的幂";
        System.out.println(msg);
    }

    // 位与运算：&，相应的每一位都是1则为1，否则为0，可以获取两个数对应位相同部分，任意数字与0位与都为0，与-1位与结果为本身。
    // 一个例子：判断数字是偶数还是奇数
    static void judgeEvenOrOdd(int num) {
        // 判断原理：一个数字与1做位与运算，由于1的二进制表示，除了最后一位其他位都是0，所以与1相同并且有效部分就在最后一位，
        // 最终结果非0即1，取决于num的最后一位，如果最后一位是0，则为偶数，位与结果为0；如果最后一位是1，则为奇数，位与结果为1。
        int result = num & 1;
        if (result == 0) {
            System.out.println(num + "是偶数");
        } else {
            System.out.println(num + "是奇数");
        }
    }

    static void surplus(int num) {
        // 取余：对2的幂进行取余，num & (2的幂 -1)
        // 例如对4取余，让num与3做位与运算：
        // 4的二进制：0000 0000 0000 0000 0000 0000 0000 0100
        // 3的二进制：0000 0000 0000 0000 0000 0000 0000 0011
        // 余数的定义是整除后被除数未除尽的部分，且余数的取值范围 [0, 除数 - 1]。
        // 2的幂在二进制中有一个特点，只存在一个1的位，其余都是0。
        // 以2的幂的二进制的角度来看，余数的范围是 [0, 2的幂 - 1]，二进制表现余数最大值为：2的幂的二进制中的高位1置为0，高位右侧全置为1。
        // 所以计算2的幂的余数也就是求出数字在高位1右侧的值。
        // 由上图二进制形式可以看出：4的二进制最后3位是100，3的二进制最后3位是 011，余数的取值范围为：[0, 3]，符合上面的推论。
        // 取余运算可以根据这个特性，只要与3进行位与运算，由于位与运算可以保留出相同部分，那么可以获取数字的后两位二进制，即为对4的取余结果。
        int remainder = num & 3;
        System.out.println(remainder);
    }

    static int MAXIMUM_CAPACITY = 1 << 30;

    static int tableSizeFor(int cap) {
        // 计算第一个大于等于cap且为2的幂的数字，在2的幂的二进制表示中，一定只有一个1，且这个数字一定为正整数。
        // 首先减去1，保证生成数字大于等于cap
        int n = cap - 1;
        // 无符号右移一位，将n的最高位移至最高位右1位，此时进行或运算，可得原最高位和右1位都是1
        n |= n >>> 1;
        // 右移两位，将上一步获得的n的最高位2位（因为上面的或运算，所以都是1），再次移动2位，进行或运算，可获得最高位右4位都是1
        n |= n >>> 2;
        // 右移四位，与上面同理，可获得最高位右8位都是1
        n |= n >>> 4;
        // 右移8位，最高位右16位都是1
        n |= n >>> 8;
        // 右移16位，最高位右32位都1，int最多32位，所以到这已经足够
        n |= n >>> 16;
        // 设计思想就是将数字的最高位右边都置为1，然后 +1 进位，得到数即满足2的幂，且是第一个大于cap的。或者取到最高位，左移一位，将右侧置0
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    // 位或运算：|，相应的每一位都是0则为0，否则为1。任意数与 0 位或，结果为本身，与 -1 位或，结果为 -1
    // 位非运算：~，相应的每一位都进行反转，包括符号位。~ 0 = -1
    // 位异或运算：^，相应的每一位不同为1，相同则为0。可以获取两个数的不同部分。
    //      1. 交换律，a ^ b = b ^ a，多个变量亦如此，a ^ b ^ c = b ^ c ^ a。
    //      2. 任何两个相同数字进行异或，结果为0。
    //      3. 对于一个二进制来说，这个位上的数与另一个二进制位上的0异或，运算结果是这个位上的数不变；
    //      如果是与二进制位上的1异或，那么结果这个位上的数为相反数，即1 -> 0, 0 -> 1。
    //      所以一个数异或0，结果为本身，一个数异或-1，结果为相反数。
    static void exchangeVariables(int x, int y) {
        // 由于一个数异或另一个数两次等于异或0，等于本身，所以下面表达式：
        // x ^= y ==>  x = x ^ y
        // y ^= x ==>  y = y ^ x  ==>  y = y ^ x ^ y ==> y = x ^ 0 = x
        // x ^= y ==>  x = x ^ y  ==>  x = x ^ y ^ x = y ^ 0 = y
        System.out.println(String.format("交换前，x = %s, y = %s", x, y));
        x ^= y;
        y ^= x;
        x ^= y;
        System.out.println(String.format("交换后，x = %s, y = %s", x, y));
    }

    static void getAbsoluteValue(int num) {
        // 获取绝对值，首先判断正负，judge只有两个可能，正数为0，负数为-1。
        int judge = num >> 31;
        // 第一种方式：判断judge的值，正数直接取本身，负数的绝对值等于负数的相反数
        int result = judge == 0 ? num : (~ num + 1);
        System.out.println(result);
        // 第二种方式：任何数与0异或都不变，与 -1 异或相当于取反。
        // 所以可以使得异或后的结果再减去judge，如果num是负数，则judge为-1，也就相当于 +1，所以就变成了 ~ num +1，正数judge为0，结果就等于num
        int result2 = (num ^ judge) - judge;
        System.out.println(result2);
        // 第三种方式：若judge为0，num为正数，以下表达式为 (num + 0) ^ 0 = num；
        // 若judge为-1，num为负数，表达式变为 (num - 1) ^ (-1) = ~ (num - 1)，
        // 由于负数的补码是在反码基础上 +1，所以 num - 1 = num 的反码，再次求反，获得num的绝对值（符号位已经反转）
        int result3 = (num + judge) ^ judge;
        System.out.println(result3);
    }

    static void getOppositeNum(int num) {
        // 相反数的定义：绝对值相等，正负号相反的两个数互为相反数。
        // 作为相反数，区别只是在正负符号不同而已，在二进制形式上，只是符号位不同。
        // 对于正数来说，其实是求出原码除符号位外与正数相同的负数。由于计算机内部都是使用补码进行运算，
        // 正数的补码和原码一样，负数则不同。负数的补码计算过程为：除符号位外都求反，然后 +1。
        // 而取反运算是将所有位都求反，可以分成两步：第一步将符号位求反，变成与正数互为相反数的负数的原码；
        // 第二步按照计算负数计算补码过程，将除符号位外其他位求反，此时只需要 +1 就是负数的补码了，所以对一个正数取反再 +1即可得到其相反数。
        // 对于负数来说，其实是求出与其原码除符号位外都相同的正数，负数的反码取反运算可以获得负数的相反数。补码 -1 再求反可以获得相反数，
        // 先对补码求反再 +1也可以获得相反数。
        int result = ~ num + 1;
        System.out.println(result);
    }

    static void isSameSymbol(int a, int b) {
        // 判断两个数字是否符号相同，即同正或同负
        // 只要判断符号位即可，由于异或相同为0，所以若符号位相同，即同符号，那么异或结果的符号位为0，结果为正数；
        // 若符号位不同，那么异或结果的符号位为1，结果为负数
        boolean same;
        if (a == b) {
            same = true;
        } else {
            same = (a ^ b) > 0;
        }
        System.out.println(same);
    }

    static void getAverage(int x, int y) {
        // 求平均值：直接使用 (x + y) / 2 或 (x + y) >> 1，在 x + y 结果超过int能表示的最大值时不可用。
        // 计算平均值时，可以直接将两数相加除以2，也可以拆分，将两个数字拆为相同部分和不同部分。
        // 举个例子，比如 14 和 12 相加求平均值，按十进制，可以拆分为 10 + 4，10 + 2，那么两数相同部分为10，(10 + 10) / 2 = 10，
        // 所以可以任意取一个10作为相同部分求出的平均值。不同部分求平均值，(4 + 2) / 2 = 3，所以最终的平均值也就是 10 + 3 = 13。
        // 按照这个思想，将x与y分成两部分来计算，第一部分为相同位部分：获取相同位，x & y，那么平均数也就等于其中任意一个；
        // 第二部分计算不同部分：获取不同部分之和，x ^ y，由于是二进制，所以不同位一定是不能进位的情况，也就是一个是1，一个是0，
        // 对于这样的部分相加结果也就是1，所以加法也可以用异或运算来代替，x + y = x ^ y。最后除以2求平均值，即为 (x + y) >> 1。
        // 将两部分加起来就是两个数的平均值。
        int average = (x & y) + ((x ^ y) >> 1);
        System.out.println(average);
    }

    static void getMaximum(int x, int y) {
        // 如果 x 大于 y，那么就应该保留x，舍弃y，(x - y) >> 31 结果是0，~ (x - y) >> 31 结果为 -1。
        // y & ((x - y) >> 31) -> y & 0 = 0，所以左半边为0。
        // x & (~ (x - y) >> 31) -> x & (-1) = x，所以 0 | x = x，最大值为x。
        // 如果 x 小于 y，同理应该保留y，舍弃x：y & ((x - y) >> 31) -> y & (-1) = y
        // x & (~ (x - y) >> 31) -> x & 0 = 0,所以 y | 0 = y，最大值为y。
        // 如果 x 等于 y，那么保留任意一个即可: y & ((x - y) >> 31) -> y & 0 = 0
        // x & (~ (x - y) >> 31) -> x & (-1) = x，所以最大值为x
        int maximum = y & ((x - y) >> 31) | x & (~ (x - y) >> 31);
        System.out.println(maximum);
    }

    static void getMinimum(int x, int y) {
        // 与求最大值思想类似，消除最大值，保留最小值。
        // 获取最小值。如果 x 大于 y：x & ((x - y) >> 31) -> x & 0 = 0，
        // y & (~ (x - y) >> 31) -> y & (-1) = y, 所以 0 | y = y，最小值为y。
        // 如果 x 小于 y：x & ((x - y) >> 31) -> x & (-1) = x，
        // y & (~ (x - y) >> 31) -> y & 0 = 0, x | 0 = x,所以最小值为x。
        // 如果 x 等于 y：x & ((x - y) >> 31) -> x & 0 = 0,
        // y & (~ (x - y) >> 31) -> y & (-1) = y, 0 | y = y，所以最小值为y。
        int minimum = x & ((x - y) >> 31) | y & (~ (x - y) >> 31);
        System.out.println(minimum);
    }

    public static void main(String[] args) {
//        intBitOperation();
//        longBitOperation();
//        otherBitOperation();
//        judgeEvenOrOdd(2);
//        surplus(7);
//        exchangeVariables(1, 2);
//        convert();
//        System.out.println("tableSizeFor: " + tableSizeFor(292));
//        isPowerOf2(128);
        getOppositeNum(-127);
//        getAbsoluteValue(-128);
//        isSameSymbol(0, -1);
//        getAverage(12, 2);
//        getMaximum(5, 5);
//        getMinimum(2, 5);
    }

}
