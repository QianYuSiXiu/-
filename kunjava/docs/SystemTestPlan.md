# 系统测试计划文档

## 1. 测试概述

本测试计划旨在全面验证kunjava系统的功能完整性、性能稳定性及用户体验质量。系统是一个基于Spring Boot的Web应用，主要功能包括用户管理、课题管理、课题选择管理、文件上传等。

## 2. 测试范围

### 2.1 功能测试范围
- 用户管理模块（注册、登录、信息更新）
- 课题管理模块（课题CRUD操作）
- 课题选择模块（用户选取课题、取消选取、查询等）
- 文件上传模块（图片上传功能）
- 管理员模块（管理员操作）

### 2.2 非功能测试范围
- 性能测试（响应时间、并发处理能力）
- 安全性测试（JWT认证、权限控制）
- 兼容性测试（不同浏览器支持）
- 可靠性测试（异常处理）

## 3. 测试策略

### 3.1 单元测试策略
- 针对每个业务逻辑层方法进行独立测试
- 验证数据访问层的CRUD操作
- 测试工具类的功能正确性

### 3.2 集成测试策略
- 验证各模块间的接口调用
- 测试前后端数据交互
- 验证数据库连接和事务处理

### 3.3 端到端测试策略
- 模拟真实用户操作流程
- 验证完整的业务场景
- 测试异常情况处理

## 4. 测试用例设计

### 4.1 用户管理模块测试

#### 4.1.1 用户注册功能测试
| 用例编号 | 测试场景 | 输入数据 | 预期结果 |
|---------|----------|----------|----------|
| TC_USER_001 | 正常用户注册 | 有效的用户名、邮箱、密码 | 注册成功，返回用户信息 |
| TC_USER_002 | 重复用户名注册 | 已存在的用户名 | 注册失败，提示用户名已存在 |
| TC_USER_003 | 空用户名注册 | 空用户名 | 注册失败，提示用户名不能为空 |
| TC_USER_004 | 无效邮箱格式 | 不符合格式的邮箱 | 注册失败，提示邮箱格式错误 |

#### 4.1.2 用户登录功能测试
| 用例编号 | 测试场景 | 输入数据 | 预期结果 |
|---------|----------|----------|----------|
| TC_LOGIN_001 | 正确账号密码 | 有效账号和正确密码 | 登录成功，返回JWT token |
| TC_LOGIN_002 | 错误密码 | 有效账号和错误密码 | 登录失败，提示密码错误 |
| TC_LOGIN_003 | 不存在的账号 | 不存在的用户名 | 登录失败，提示用户不存在 |

### 4.2 课题管理模块测试

#### 4.2.1 课题CRUD操作测试
| 用例编号 | 测试场景 | 输入数据 | 预期结果 |
|---------|----------|----------|----------|
| TC_PROJECT_001 | 创建课题 | 有效的课题信息 | 课题创建成功，返回课题ID |
| TC_PROJECT_002 | 更新课题 | 有效的课题ID和更新信息 | 课题更新成功 |
| TC_PROJECT_003 | 删除课题 | 有效的课题ID | 课题删除成功 |
| TC_PROJECT_004 | 查询课题列表 | 分页参数 | 返回课题列表 |

### 4.3 课题选择模块测试

#### 4.3.1 课题选择功能测试
| 用例编号 | 测试场景 | 输入数据 | 预期结果 |
|---------|----------|----------|----------|
| TC_SELECT_001 | 用户选取课题 | 有效的用户ID和课题ID | 选取成功，记录插入数据库 |
| TC_SELECT_002 | 重复选取同一课题 | 已选取的用户课题组合 | 选取失败，提示已选取 |
| TC_SELECT_003 | 取消选取课题 | 有效的用户课题组合 | 取消成功，记录删除 |
| TC_SELECT_004 | 查询用户已选课题 | 有效的用户ID | 返回已选课题列表 |

### 4.4 文件上传模块测试

#### 4.4.1 图片上传功能测试
| 用例编号 | 测试场景 | 输入数据 | 预期结果 |
|---------|----------|----------|----------|
| TC_UPLOAD_001 | 正常图片上传 | 有效的图片文件 | 上传成功，返回文件路径 |
| TC_UPLOAD_002 | 非图片文件上传 | PDF、DOC等非图片文件 | 上传失败，提示文件格式错误 |
| TC_UPLOAD_003 | 超大文件上传 | 超过限制大小的文件 | 上传失败，提示文件过大 |
| TC_UPLOAD_004 | 空文件上传 | 空文件 | 上传失败，提示请选择文件 |

## 5. 自动化测试脚本

### 5.1 单元测试示例

```java
// UserServiceTest.java
@SpringBootTest
public class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @MockBean
    private UserMapper userMapper;
    
    @Test
    public void testSaveUserWithAutoDate() {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        
        boolean result = userService.save(user);
        
        assertTrue(result);
        assertNotNull(user.getRegistration_date());
        assertNotNull(user.getLast_login_time());
    }
    
    @Test
    public void testFindUserById() {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setUsername("testuser");
        when(userMapper.selectById(1)).thenReturn(mockUser);
        
        User result = userService.getById(1);
        
        assertEquals("testuser", result.getUsername());
    }
}
```

### 5.2 集成测试示例

```java
// ProjectControllerTest.java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProjectControllerTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void testGetAllProjects() {
        ResponseEntity<Project[]> response = restTemplate.getForEntity(
            "/api/project/list", Project[].class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().length >= 0);
    }
    
    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("Test Description");
        
        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/project/save", project, String.class);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
```

### 5.3 功能测试示例

```java
// ProjectSelectFeatureTest.java
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProjectSelectFeatureTest {
    
    @Autowired
    private ProjectSelectService projectSelectService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ProjectService projectService;
    
    @Test
    public void testUserSelectProject() {
        // 准备测试数据
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        userService.save(user);
        
        Project project = new Project();
        project.setTitle("Test Project");
        project.setDescription("Test Description");
        projectService.save(project);
        
        // 执行选取操作
        boolean result = projectSelectService.selectProject(user.getId(), project.getId());
        
        assertTrue(result);
        
        // 验证选取记录存在
        List<ProjectSelect> selections = projectSelectService.getUserSelectedProjects(user.getId());
        assertEquals(1, selections.size());
    }
}
```

## 6. 测试执行计划

### 6.1 测试阶段安排
1. 第一阶段：单元测试（2天）
2. 第二阶段：集成测试（3天）
3. 第三阶段：系统测试（3天）
4. 第四阶段：验收测试（2天）

### 6.2 测试人员分工
- 开发团队：负责单元测试
- QA团队：负责集成测试和系统测试
- 产品经理：负责验收测试

## 7. 测试环境要求

### 7.1 硬件环境
- CPU：Intel i5以上
- 内存：8GB以上
- 存储：100GB可用空间

### 7.2 软件环境
- 操作系统：Windows 10/11, Linux, macOS
- JDK版本：17+
- 数据库：MySQL 8.0+
- 应用服务器：Tomcat 9+

## 8. 测试通过标准

- 单元测试覆盖率≥80%
- 集成测试通过率≥95%
- 系统测试通过率≥95%
- 关键功能缺陷数量≤3个
- 性能指标满足需求规格

## 9. 风险评估

- 数据库连接不稳定可能影响测试结果
- 第三方依赖服务不可用影响部分功能测试
- 并发用户数超过预期可能影响性能测试结果