package spark.util

import redis.clients.jedis.{JedisPool, JedisPoolConfig}

/**
  * Created by gongp on 2018/9/26.
  */
object RedisClient extends Serializable{

  private var MAX_IDLE :Int =200
  private var TIMEOUT:Int =10000
  private var TEST_ON_BORROW:Boolean =true

  lazy val config:JedisPoolConfig={
    val config= new JedisPoolConfig
    config.setMaxIdle(MAX_IDLE)
    config.setTestOnBorrow(TEST_ON_BORROW)
    config
  }

  lazy val pool = new JedisPool(config,KafkaRedisProperties.REDIS_SERVER,KafkaRedisProperties.REDIS_PORT,TIMEOUT)

  lazy val hook = new Thread{
    override def run(): Unit = {
      println("execute hook Thread"+this)
      pool.destroy()
    }
  }
  sys.addShutdownHook(hook.run())


  def main(args: Array[String]): Unit = {
    val jedis = RedisClient.pool.getResource
    println(jedis.dbSize())
    jedis.close()
  }
}
