local lockKey = KEYS[1]
local lockValue = KEYS[2]
local result = redis.call('bf.add', lockKey, lockValue)
return result

