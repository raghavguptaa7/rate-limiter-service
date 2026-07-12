local key = KEYS[1]

local currentTime = tonumber(ARGV[1])

local capacity = tonumber(redis.call('HGET', key, 'capacity'))
local tokens = tonumber(redis.call('HGET', key, 'tokens'))
local refillRate = tonumber(redis.call('HGET', key, 'refillRate'))
local lastRefillTime = tonumber(redis.call('HGET', key, 'lastRefillTime'))

if capacity == nil then
    return -1
end

local elapsedSeconds = math.floor((currentTime - lastRefillTime) / 1000)

if elapsedSeconds > 0 then

    local newTokens = elapsedSeconds * refillRate

    tokens = math.min(capacity, tokens + newTokens)

    lastRefillTime = lastRefillTime + elapsedSeconds * 1000

end

if tokens <= 0 then

    redis.call('HSET', key, 'tokens', tokens)
    redis.call('HSET', key, 'lastRefillTime', lastRefillTime)

    return -2

end

tokens = tokens - 1

redis.call('HSET', key, 'tokens', tokens)
redis.call('HSET', key, 'lastRefillTime', lastRefillTime)

return tokens