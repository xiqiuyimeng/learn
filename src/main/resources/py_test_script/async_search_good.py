# -*- coding: utf-8 -*-
import asyncio
import json

from datetime import datetime
from aiohttp import ClientSession


# 压力测试，模拟测试并发量比较高的情况下，缓存击穿、缓存穿透问题

true = True
false = False
success_num = 0
fail_num = 0

start = datetime.now()


async def get(req_url):
    print(f'请求 {req_url} 开始')
    async with ClientSession() as session:
        async with session.get(req_url) as response:
            res = await response.read()
            text = res.decode("utf8")
            success = json.loads(text).get("success")
            if success:
                global success_num
                success_num += 1
            else:
                global fail_num
                fail_num += 1
    print(f'请求 {req_url} 结束，结果：{text}\n')


url = "http://localhost:8080/good/getGoodByName?goodName=商品2"
# 并发请求200次
tasks = [get(url) for i in range(10)]
loop = asyncio.get_event_loop()
loop.run_until_complete(asyncio.wait(tasks))

end = datetime.now()
time = end - start
print(f"查询成功次数：{success_num}，查询失败次数：{fail_num}，总耗时：{time}")
