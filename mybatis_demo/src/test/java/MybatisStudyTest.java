
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageInterceptor;
import com.mysql.cj.jdbc.MysqlDataSource;
import com.mysql.cj.util.StringUtils;
import com.sun.jdi.LongType;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.SimpleExecutor;
import org.apache.ibatis.executor.result.DefaultResultHandler;
import org.apache.ibatis.io.DefaultVFS;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.ognl.Ognl;
import org.apache.ibatis.ognl.OgnlException;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.*;
import org.demo.builder.MyEntityBuilderTest;
import org.demo.entity.User;
import org.demo.mapper.UserMapper;
import org.demo.utils.JDBCUtils;
import org.demo.utils.MyTypeHandler;
import org.demo.utils.SessionUtils;
import org.demo.utils.XpathUtil;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName MybatisStudyTest
 * @description: TODO
 * @author: suhaoran
 * @date 2023年07月19日
 * @version: 1.0
 */
public class MybatisStudyTest {

    @Test
    public void test1() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = new User();
        user.setAge(7);
        //user.setName("哆啦A梦077");
//        user.setBirthday(new Date());
        int count = mapper.insert(user);
        System.out.println(count);
        sqlSession.commit();
    }

    @Test
    public void test2() throws IOException {
        SqlSession sqlSession = SessionUtils.getSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectById(2L,"shr");
        System.out.println(user);
    }

    @Test
    public void test3() throws IOException {
        SqlSession sqlSession = SessionUtils.getSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        Page<Object> page = PageHelper.startPage(1, 2);
        List<User> list = mapper.list();
        System.out.println(list);
        System.out.println(page);
    }

    @Test
    public void test4() throws IOException {
        SqlSession sqlSession = SessionUtils.getSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        //开启分页插件,第一页 每页两条
        PageHelper.startPage(1, 2);
        List<User> list = mapper.list();
        PageInfo<User> userPageInfo = new PageInfo<>(list,4);
        System.out.println(userPageInfo);
    }

    @Test
    public void xpathTest1()throws Exception{
        DocumentBuilder builder = XpathUtil.getDocumentBuilder();

        // 将文档加载到一个document对象
        Document document = builder.parse("E:\\study\\mybatis_demo\\src\\main\\resources\\xpath.xml");
        // 创建xpathfactory
        XPathFactory factory = XPathFactory.newInstance();
        // 创建xpath
        XPath xPath = factory.newXPath();
        // 编译xpath表达式
        XPathExpression expression = xPath.compile("//book[author='qiuzhangwei']/title/text()");
        // 通过xpath表达式得到劫夺，第一个参数指定的是xpath表达式进行查询的上下文节点；第二个参数参数指定了xpath表达式的返回类型
        // 其他四种类型有boolean、string、number、Node
        Object result = expression.evaluate(document, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;// 强制类型转换
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println("查询作者为qiuzhangwei的图书标题是：" + nodes.item(i).getNodeValue());
        }
        System.out.println("----------------------------------------------");
        // 减少编译
        nodes = (NodeList) xPath.evaluate("//book[@year>2000]/title/text()", document, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            System.out.println("2000年后图书属性和标题：" + nodes.item(i).getNodeValue());
        }
        System.out.println("----------------------------------------------");
        NodeList evaluate =(NodeList)xPath.evaluate("//book/price/text()", document, XPathConstants.NODESET);
        System.out.println(evaluate);
        for (int i = 0; i < evaluate.getLength(); i++) {
            System.out.println("价钱：" + evaluate.item(i).getNodeValue());
        }
        System.out.println("----------------------------------------------");
        NodeList pictures =(NodeList)xPath.evaluate("//author//name/text()", document, XPathConstants.NODESET);
        System.out.println(evaluate);
        for (int i = 0; i < pictures.getLength(); i++) {
            System.out.println("作者：" + pictures.item(i).getNodeValue());
        }

        System.out.println("----------------------------------------------");
        Document document1 = builder.parse("E:\\study\\mybatis_demo\\src\\main\\resources\\mybatis\\mybatis-config.xml");
        XPathFactory factory1 = XPathFactory.newInstance();
        XPath xPath1 = factory1.newXPath();
        NodeList xml =(NodeList)xPath1.evaluate("//settings/setting/@name",document1,XPathConstants.NODESET);
        for (int i = 0; i < xml.getLength(); i++) {
            System.out.println("作者：" + xml.item(i).getNodeValue());
        }
    }


    @Test
    public void xpathTest2()throws Exception{
        InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml");
        XPathParser xPathParser = new XPathParser(inputStream, true, null, new XMLMapperEntityResolver());
        List<XNode> xNodes = xPathParser.evalNodes("//settings/setting");
        xNodes.forEach(xn->{
            System.out.println(xn.evalString("@name"));
            System.out.println(xn.evalString("@value"));
        });
        System.out.println(xNodes);
    }


    @Test
    public void configurationTest(){

        //设置环境
        Configuration configuration = new Configuration();
        JdbcTransactionFactory transactionFactory = new JdbcTransactionFactory();
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        Environment environment = new Environment("dev",transactionFactory,mysqlDataSource);


        configuration.setEnvironment(environment);

        //设置properties
        Properties properties = new Properties();
        properties.setProperty("user","root");

        configuration.setVariables(properties);

        //设置settings
        configuration.setCacheEnabled(true);
        configuration.setUseGeneratedKeys(true);
        configuration.setDefaultExecutorType(ExecutorType.SIMPLE);
        configuration.setLogImpl(Slf4jImpl.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        //设置typeAlias
        TypeAliasRegistry typeAliasRegistry = configuration.getTypeAliasRegistry();
        typeAliasRegistry.registerAlias("user",User.class);
        typeAliasRegistry.registerAliases("org.demo.entity");

        //设置mapper
        MapperRegistry mapperRegistry = configuration.getMapperRegistry();
        mapperRegistry.addMapper(UserMapper.class);

        //添加plugin  mybatis配置的plugin实际上是拦截器
        PageInterceptor pageInterceptor = new PageInterceptor();
        configuration.addInterceptor(pageInterceptor);

        //添加类型转换器
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        typeHandlerRegistry.register(MyTypeHandler.class);

    }


    @Test
    public void ognlTest() throws OgnlException {
        User user = new User();
        user.setBirthday(new Date());
        user.setName("马户");
        user.setAge(19);
        user.setId(3l);
        Object name = Ognl.getValue("name", user);
        System.out.println(name);


    }


    @Test
    public void metaObjectTest(){
        User user = new User();
        user.setBirthday(new Date());
        user.setName("马户");
        user.setAge(19);
        user.setId(3l);
        MetaObject metaObject = MetaObject.forObject(user,new DefaultObjectFactory(),new DefaultObjectWrapperFactory(),new DefaultReflectorFactory());
        metaObject.setValue("name","又鸟");
        System.out.println(user);
        metaObject.setValue("dept.name","罗刹海市");
        System.out.println(user);
        Object value = metaObject.getValue("dept.name");
        System.out.println(value);
        MetaObject metaObject1 = SystemMetaObject.forObject(user);
        metaObject1.setValue("age",10);
        System.out.println(user);

    }


    @Test
    public void testReflect() throws Exception {
        Class<?> clazz = Class.forName("org.demo.entity.User");
        System.out.println(clazz);
        Class userClazz = User.class;
        System.out.println(userClazz);
        User user = new User();
        Class userClazz1 = user.getClass();
        Method setAge = userClazz1.getMethod("setAge", Integer.class);
//        for(int i = 0 ;i<methods.length;i++){
//            System.out.println(methods[i]);
//        }
        Object o = userClazz1.getDeclaredConstructor(String.class,Integer.class,Date.class).newInstance("小胡",Integer.valueOf(19),new Date());
        System.out.println(o);
        setAge.invoke(o,Integer.valueOf(59));
        System.out.println(o);

        Class<?>[] parameterTypes = setAge.getParameterTypes();
        for(int i = 0 ;i<parameterTypes.length;i++){
            System.out.println(parameterTypes[i]);
        }

        Method[] declaredMethods = userClazz1.getDeclaredMethods();
        for(int i = 0 ;i<declaredMethods.length;i++){
            System.out.println(declaredMethods[i]);
        }
    }

    @Test
    public void testErrorContext() throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i=0;i<10;i++){
            final int j =i;
            service.execute(()->{
                try{
                    ErrorContext instance = ErrorContext.instance()
                            .activity("在第"+j+"个线程中")
                            .object(this.getClass().getName())
                            .sql("select ** from user")
                            .resource("user.xml");
                    if(new Random().nextInt(10)>6){
                        int m = 1/0;
                    }
                }catch (Exception e){
                    throw ExceptionFactory.wrapException("sql error",e);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }

    @Test
    public void testMappedStatement() throws IOException {

        InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml");
        Configuration configuration = new XMLConfigBuilder(inputStream).parse();

        String sql = "select * from user where id=#{id}";

        List<ParameterMapping> parameterMappings = new ArrayList<>();
        ParameterMapping.Builder parameterMappingBuilder = new ParameterMapping.Builder(configuration,"name",String.class);
        ParameterMapping nameParameterMapping = parameterMappingBuilder.build();
        parameterMappings.add(nameParameterMapping);

        StaticSqlSource sqlSource = new StaticSqlSource(configuration,sql,parameterMappings);

        MappedStatement.Builder mappedStatementBuilder = new MappedStatement.Builder(configuration, "selectAll", sqlSource, SqlCommandType.SELECT);


        List<ResultMapping> resultMappings = new ArrayList<>();
        ResultMapping nameResultMapping = new ResultMapping.Builder(configuration, "name", "name", String.class).build();
        resultMappings.add(nameResultMapping);
        ResultMapping ageResultMapping = new ResultMapping.Builder(configuration, "age", "age", Integer.class).build();
        resultMappings.add(ageResultMapping);
        ResultMapping birthdayResultMapping = new ResultMapping.Builder(configuration, "birthday", "birthday",Date.class).build();
        resultMappings.add(birthdayResultMapping);
        ResultMapping idResultMapping = new ResultMapping.Builder(configuration, "id", "id",Long.class).build();
        resultMappings.add(idResultMapping);

        ResultMap.Builder resultMapBuilder = new ResultMap.Builder(configuration,"UserResult",User.class,resultMappings);
        ResultMap resultMap = resultMapBuilder.build();


        List<ResultMap> resultMaps = new ArrayList<>();
        resultMaps.add(resultMap);

        mappedStatementBuilder.resultMaps(resultMaps);
        MappedStatement mappedStatement = mappedStatementBuilder.build();

        User user = new User();
        user.setId(7L);
        BoundSql boundSql = mappedStatement.getBoundSql(user);
        String boundSqlStr = boundSql.getSql();
        System.out.println(boundSqlStr);

    }

    @Test
    public void testDynamicContext(){
        //解析动态sql 拼接sql语句
    }

    @Test
    public void testStringJoiner(){
        StringJoiner stringJoiner = new StringJoiner(",");
        stringJoiner.add("1");
        stringJoiner.add("2");

        System.out.println(stringJoiner.toString());

        String ab = String.join(",", "ab","cd","ef","jh");
        System.out.println(ab);
    }

    //加载classpath下的文件
    @Test
    public void testVfs() throws IOException {
        DefaultVFS defaultVFS = new DefaultVFS();
        List<String> list = defaultVFS.list("org/demo/config");
        list.forEach(vfs->{
            System.out.println(vfs);
        });

        List<String> jar = defaultVFS.list(new URL("file:E:\\repo\\mysql\\mysql-connector-java\\8.0.29\\mysql-connector-java-8.0.29.jar"), "com/mysql/cj/jdbc/");

        jar.forEach(j->{
            System.out.println(j);
        });


    }

    @Test
    public void testExcutor() throws IOException, SQLException {
        InputStream inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml");
        Configuration configuration = new XMLConfigBuilder(inputStream).parse();
        TransactionFactory transactionFactory = configuration.getEnvironment().getTransactionFactory();
        DataSource dataSource = configuration.getEnvironment().getDataSource();

        Transaction transaction = transactionFactory.newTransaction(dataSource, TransactionIsolationLevel.READ_COMMITTED, true);
        SimpleExecutor simpleExecutor = new SimpleExecutor(configuration,transaction);
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",2);
        map.put("name","test");
        RowBounds rowBounds = new RowBounds();
        DefaultResultHandler defaultResultHandler = new DefaultResultHandler();
        List<Object> query = simpleExecutor.query(configuration.getMappedStatement("org.demo.mapper.UserMapper.selectById"), map, rowBounds, defaultResultHandler);
        System.out.println(query);
        List<Object> resultList = defaultResultHandler.getResultList();
        resultList.forEach(System.out::println);


    }

    @Test
    public void testProceDure() throws IOException {
        SqlSession sqlSession = SessionUtils.getSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.testProceDure(20L);
        System.out.println(user);
    }

    @Test
    public void testCallAble() throws SQLException {
        Connection connection = JDBCUtils.getConnection();
        CallableStatement callableStatement = connection.prepareCall("call getAllUser()");
        callableStatement.execute();
        ResultSet resultSet = callableStatement.getResultSet();
        while (resultSet.next()){
            System.out.println(resultSet.getLong("id"));
            System.out.println(resultSet.getString("name"));
            System.out.println(resultSet.getInt("age"));
            System.out.println(resultSet.getDate("birthday"));
            System.out.println("----------------------------------");
        }


    }


    @Test
    public void testMyEntityBuilderTest(){
        MyEntityBuilderTest.Builder shr = new MyEntityBuilderTest.Builder(1L, "").setNum(10);
        MyEntityBuilderTest build = shr.build();
        System.out.println(build);
    }



    public static void main(String[] args) {
        List list=null;
        Optional<List> s = Optional.ofNullable(list);
        boolean present = s.isPresent();
        System.out.println(present);
        Optional<String> s1 = Optional.ofNullable("");
        System.out.println(s1.isEmpty());
        System.out.println(s1.isPresent());

        String str = ",,s,,\\,,";
        String trim = str.trim();
        str = str.replaceAll("\\\\","a");

        System.out.println(str);

        String data1 = "\"65656565\"";
        data1 = data1.replace("\"\"", "");
        System.out.println(data1);


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:SS");
        Date date = new Date("2023/9/27 15:00:00");
        String format = simpleDateFormat.format(date);
        System.out.println(format);

    }


}
