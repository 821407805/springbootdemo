local lockKey = KEYS[1]
local lockValue = KEYS[2]
local result = redis.call('bf.exists', lockKey, lockValue)
return result

