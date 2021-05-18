CREATE TABLE `good` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `good_name` varchar(50) NOT NULL COMMENT '商品名称',
  `store` int(11) NOT NULL COMMENT '库存量',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `idx` (`good_name`,`store`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;