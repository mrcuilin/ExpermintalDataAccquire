这里保存与项目相关的基础类与基础常量等
连接池保存对不同的数据库的连接池，这些连接池都应该在主配置文件中说明，使用不同的字段区分开
每个DAO对象一定会对应一个的数据库，所以把对应的数据库的连接池预置在DAO对象中，方便后续使用