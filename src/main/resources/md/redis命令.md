##### redis命令：

1. 字符串：
   
   - 获取value -> get key，存value -> set key name
   
   - 自动增长： incr key，默认增加数字是1，如果想自定义数字：incrby key num，设置num即可。
   - 递减的用法：decr key,  也是默认增加1，自定义：decrby key num。
   - 增加浮点数：incrbyfloat key num
   - 尾部追加值：append key value
   - 获取字符串长度：strlen key
   - 批量设置值/取值：mset key1 value1 key2 value2 key3 value3。mget key1 key2 key3
   
2. 散列类型：

   - 赋值：hset key field value
   - 取值：hget key field
   - 批量赋值：hmset key field value field value field value
   - 批量取值：hmget key field field field
   - 获取所有字段值对：hgetall key （返回值奇数为field，偶数为value）
   - 判断字段是否存在：hexists key field （返回值0否1是）
   - 字段不存在时赋值：hsetnx key field value （当field不存在时才会赋值，存在则什么都不做）
   - 增加数字：hincrby key field increment （增长increment数字，前提是值必须是可增长的数字）
   - 删除字段：hdel key field
   - 只获取key：hkeys key
   - 只获取value：hvals key
   - 获取字段数量：hlen key
   
3. 列表类型：
   - 列表两端添加元素：
     - lpush key value
     - rpush key value
   - 列表两端弹出元素：
     - lpop key
     - rpop key
   - 获取列表元素个数：llen key
   - 获取一段元素：lrange key start stop （索引从0开始，范围是闭区间，想获取整个列表，可以0 ，-1）
   - 删除元素：lrem key count value
     - count > 0 时，从列表左边向右删除count个value
     - count < 0 时，从列表右边向左删除count个value
     - count = 0时， 删除所有值为value的元素
   - 获取指定索引元素：lindex key index
   - 给指定索引赋值：lset key index value（可以认为是在修改原索引位置上的值，不能给不存在的索引赋值）
   - 只保留指定范围的元素：ltrim key start end （也是闭区间）
   - 向列表插入元素：linsert key before | after pivot value（pivot是要插入的参考值）
   - 元素从一个列表转移到另一个列表：rpoplpush source destination（从source列表的右边弹出并追加到destination的左边）
   
4. 集合类型：

   - 增加元素：sadd key member
   - 删除元素：srem key member
   - 获取所有元素：smembers key
   - 判断元素是否在集合中：sismember key member
   - 求差集：sdiff keyA keyB （A - B，属于A不属于B的元素）
   - 求交集：sinter keyA keyB （A与B的交集，同时属于A,B的元素）
   - 求并集：sunion keyA keyB （A与B的并集，所有属于A与B的元素）
   - 获取集合元素数：scard key
   - 将差集运算结果储存：sdiffstore destination keyA keyB（将AB的差集存入destination而不输出到屏幕）
   - 将交集运算结果储存：sinterstore destination keyA keyB (将AB的交集存入destination不输出到屏幕)
   - 将并集运算结果储存：sunionstore destination keyA keyB (将AB的并集存入destination不输出到屏幕)
   - 随机获取元素：srandmember key count 
     - count > 0：获取count个元素（不重复）
     - count < 0 ：获取count个元素（可能重复）
   - 随机弹出一个元素：spop key count（指定数量）

5. 有序集合类型：

   - 增加元素：zadd key score member （也可以通过此命令修改某个元素的分数）

   - 获取元素的分数：zscore key member

   - 按索引取元素：zrange key start stop 【withscores】（加withscores可以显示出分数）

   - 逆向按索引取元素：zrevrange key start stop 【withscores】（如果start为0，就是最后一个，1代表倒数第二个。。。。。）

   - 按分数取元素：zrangebyscore key min max（如果希望不包含min或者max分数，在相应分数前加一个“（”即可。）若要求所有范围：min可以设为 -inf ，max设为 + inf（无穷）

   - 按分数逆向取元素：zrevrangebyscore key max min（都可以后面接limit offset count）

   - 给某元素增加分数：zincrby key increment member

   - 获取集合元素数量：zcard key

   - 获取指定分数范围内元素数量：zcount key min max（同样也可以加“（”指定不包含min或者max）

   - 删除元素：zrem key member

   - 按照排名范围删除：zremrangebyrank key start stop（其实是按分数排序后的索引）

   - 按照分数范围删除：zremrangebyscore key min max（发现分数都可以指定“（”不包含的边界）

   - 获取元素的排名：zrank key member（其实是索引位置）

   - 获取元素的逆向排名：zrevrank key member

   - 计算交集并储存：和集合类似 zinterstore destination numkeys keyA keyB [aggregate sum|min|max] （numkeys是代表多少个集合参与运算）

     - 当aggregate为sum（默认）：新元素的分数为对应元素的分数相加
     - aggregate为min，新元素的分数为对应元素分数求最小值
     - aggregate为max，新元素的分数为对应元素分数求最大值

   - 计算并集并储存：zunionstore destination numkeys keyA keyB （与交集上述规则相同，需要注意的是没有差集计算）

     ------

     

- ###### 事务：

​	事务写法：multi

​						语句1

​						语句2

​						。。。。

​						exec

只能保证顺序执行，如果出错，也没有回滚机制，所以需要自己来控制回滚。

- 过期时间：expire key second（或者pexpire毫秒级别）
- 查询还有多久过期：ttl key（如果是pexpire，需要用pttl）
- 使键永久不过期：persist key（在有过期时间后，可以用这个命令取消掉过期时间或者set赋值后也会清除过期时间）
- sort ：排序方法，可以设置limit offset count，
  - by 参数，by的作用是用另外的一批key来作为参考，对当前key进行排序。由于命名规范为“：”分隔，比如第一位为统一的名字，第二位为id，用hash为例，key为：name：num，field存储具体的字段名，比如sortby ，value为值。那么排序时sort key by （后面跟的是一个模式匹配）name：*->sortby，这样会读取每个key中的元素来替换掉 *，然后去读取替换后的值，以此值来排序key。这样命名好处是容易匹配易读。如果不存在 *，则为常量名，那就不会再排序了。
  - get参数：在sort后返回特定的键值。以上面的例子，如果在name中还有一个title字段，可以sort key by name：*->sortby get name： *-> title，这样会返回排序后的相应的title字段。get #会返回元素本身的值，也就是被排序的那个值。
  - store 参数：默认sort后的结果都是返回到屏幕，可以用store 参数将结果存储到一个键中，为list类型，只需要在sort语句后面接一个store destkey。
- 