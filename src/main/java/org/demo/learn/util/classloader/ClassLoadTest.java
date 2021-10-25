package org.demo.learn.util.classloader;

import org.demo.learn.util.classloader.test.TestA;
import org.demo.learn.util.classloader.test.TestB;

/**
 * @author luwt
 * @date 2021/4/30.
 * 类加载机制
 *      1. jvm启动加载：加载、验证、准备、解析、初始化
 *          · 加载：通过类的完全限定查找此类字节码文件，并利用字节码文件创建一个Class对象
 *          · 验证：确保Class文件的字节流中包含信息符合当前虚拟机要求，不会危害虚拟机自身安全：文件格式、元数据、字节码、符合引用验证
 *          · 准备：为类变量（static修饰的字段变量）分配内存并设置初始值（对象类型为null，基本类型为0），使用final修饰的static变量在编译期分配。
 *                  类变量将分配在方法区，实例变量分配在堆中，所以不会分配实例变量的值。
 *          · 解析：主要将常量池中的符号引用替换为直接引用的过程，符号引用就是一组符号来描述目标，可以是任何字面量，而直接引用就是直接指向目标的指针、
 *                  相对偏移量或一个间接定位到目标的句。还有类或接口解析、字段解析、类方法解析、接口方法解析。
 *          · 初始化：类加载的最后阶段，若该类具有超类，则对其进行初始化，执行静态初始化器和静态初始化成员变量（如前面只初始化了默认值的static变量
 *                  将会在这个阶段赋值，成员变量也将被初始化）
 *      2. Class.forName：默认会执行初始化块
 *      3. ClassLoader：加载类的时候不会执行初始化块
 */
public class ClassLoadTest {

    public static void main(String[] args) throws Exception {
        loadClassByName();
//        loadClassByClassLoader();
    }

    static void loadClassByName() throws Exception {
        // 普通加载，会执行初始化块
        Class<TestA> aClass = (Class<TestA>) Class.forName("org.demo.learn.util.classloader.test.TestA");

        // 指定不执行初始化块
        Class<?> bClass = Class.forName("org.demo.learn.util.classloader.test.TestB", false, TestB.class.getClassLoader());
        System.out.println("B类要执行初始化块了");
        // 此时执行初始化块
        TestB testB = (TestB) bClass.newInstance();
    }

    static void loadClassByClassLoader() throws Exception {
        // 获取类加载器
        ClassLoader loader = TestA.class.getClassLoader();
        // 加载类，不会执行初始化块
        Class<?> aClass = loader.loadClass("org.demo.learn.util.classloader.test.TestA");
        System.out.println("A类要执行初始化块了");
        // 此时执行初始化块
        aClass.newInstance();
    }


}
