- 反引号``中的命令会被替换成其执行结果。

- $? 可以获取上一条命令的执行结果，0为正常，其余为错误。

- 文件权限：

  - 获取文件权限的数字形式：stat -c $a filename

- 变量的算术运算：

  - 反引号形式：b=`expr $a + 1`
  - 双括号形式：b=$(( a + 1 ))
  - let形式：let b=a+1

- if判断：

  - 判断变量是否存在：可以直接使用变量，if [ $var ]; then echo $var; fi
  - 使用逻辑运算符：需要将中括号写成双层：if [[  $var1 && $var2 ]]

- for循环：

  - 循环数组（数组只支持一维）

    array=(a b c)

    for var in ${array[@]}; do echo $var; done

  - 循环命令结果：

    使用反引号获取命令执行结果，作为数组进行循环。

    for file in `ls`; do echo $file; done

- 函数：

  - 定义：function foo() {echo "hello world"}

    内部打印的就可以作为函数的执行结果，可以用来进行其他操作，例如循环。

  - 调用：调用函数不需要括号，无论有没有参数。

    - 无参数：foo 

    - 有参数：function foo2(){ echo $1 }，

      调用：foo2 hello

  - 返回：函数的返回值只能是数字。可以将函数想要返回的结果进行打印，也可以作为返回结果进行使用。

  
