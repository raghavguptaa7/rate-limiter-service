local tokens = tonumber(redis.call('HGET', KEYS[1], 'tokens'))
local capacity = tonumber(redis.call('HGET', KEYS[1], 'capacity'))
local refillRate = tonumber(redis.call('HGET', KEYS[1], 'refillRate'))
local lastRefillTime = tonumber(redis.call('HGET', KEYS[1], 'lastRefillTime'))

if tokens == nil then
    return -1
end

local currentTime = tonumber(ARGV[1])

local elapsed = math.floor((currentTime - lastRefillTime) / 1000)

if elapsed > 0 then

    tokens = math.min(
        capacity,
        tokens + elapsed * refillRate
    )

    lastRefillTime =
        lastRefillTime + elapsed * 1000

end

if tokens <= 0 then

    redis.call('HSET', KEYS[1],
        'tokens', tokens,
        'lastRefillTime', lastRefillTime)

    return 0

end

tokens = tokens - 1

redis.call('HSET', KEYS[1],
    'tokens', tokens,
    'lastRefillTime', lastRefillTime)

return tokens