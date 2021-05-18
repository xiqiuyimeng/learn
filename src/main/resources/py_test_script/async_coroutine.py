# -*- coding: utf-8 -*-
import asyncio
import requests
from aiohttp import ClientSession
from datetime import datetime
_author_ = 'luwt'
_date_ = '2019/3/13 18:18'


# 数据初始化
# url2 = "http://localhost:8080/good/add"
# data = {
#     "goodName": "商品",
#     "store": 50
# }
# good_name = data["goodName"]
# for i in range(1, 11):
#     data["goodName"] = good_name + str(i)
#     requests.post(url2, json=data)


# 并发请求接口，压力测试

success_num = 0
fail_num = 0

start = datetime.now()


async def get(req_url):
    print(f'请求 {req_url} 开始')
    async with ClientSession() as session:
        async with session.get(req_url) as response:
            res = await response.read()
            text = res.decode("utf8")
            if text.endswith("成功"):
                global success_num
                success_num += 1
            else:
                global fail_num
                fail_num += 1
    print(f'请求 {req_url} 结束，结果：{text}\n')


url = "http://centos121/good/buy?goodName=商品1"
# 并发请求200次
tasks = [get(url) for i in range(200)]
loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))

end = datetime.now()
time = end - start
print(f"抢购成功次数：{success_num}，抢购失败次数：{fail_num}，总耗时：{time}")


# 重置库存
def reset_store():
    print("重置库存")
    reset_url = "http://centos121/good/resetStore?store=50"
    res = requests.get(reset_url)
    print(res.text)


reset_store()
