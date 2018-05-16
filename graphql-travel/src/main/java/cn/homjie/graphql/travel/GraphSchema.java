package cn.homjie.graphql.travel;

import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLOutputType;
import graphql.schema.GraphQLSchema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * @author jiehong.jh
 * @date 2018/5/16
 */
public class GraphSchema {
    @Getter
    private GraphQLSchema schema;

    private GraphQLOutputType userType;

    private void initOutputType() {
        /**
         * 会员对象结构
         */
        userType = newObject()
            .name("User")
            .field(newFieldDefinition().name("id").type(GraphQLInt).build())
            .field(newFieldDefinition().name("age").type(GraphQLInt).build())
            .field(newFieldDefinition().name("sex").type(GraphQLInt).build())
            .field(newFieldDefinition().name("name").type(GraphQLString).build())
            .field(newFieldDefinition().name("pic").type(GraphQLString).build())
            .build();
    }

    public GraphSchema() {
        initOutputType();
        schema = GraphQLSchema.newSchema().query(newObject()
            .name("GraphQuery")
            .field(createUsersField())
            .field(createUserField())
            .build()).build();
    }

    /**
     * 查询单个用户信息
     *
     * @return
     */
    private GraphQLFieldDefinition createUserField() {
        return newFieldDefinition()
            .name("user")
            .argument(newArgument().name("id").type(GraphQLInt).build())
            .type(userType)
            .dataFetcher(environment -> {
                // 获取查询参数
                int id = environment.getArgument("id");

                // 执行查询, 这里随便用一些测试数据来说明问题
                User user = new User();
                user.setId(id);
                user.setAge(id + 15);
                user.setSex(id % 2);
                user.setName("Name_" + id);
                user.setPic("pic_" + id + ".jpg");
                return user;
            })
            .build();
    }

    /**
     * 查询多个会员信息
     *
     * @return
     */
    private GraphQLFieldDefinition createUsersField() {
        return newFieldDefinition()
            .name("users")
            .argument(newArgument().name("page").type(GraphQLInt).build())
            .argument(newArgument().name("size").type(GraphQLInt).build())
            .argument(newArgument().name("name").type(GraphQLString).build())
            .type(new GraphQLList(userType))
            .dataFetcher(environment -> {
                // 获取查询参数
                int page = environment.getArgument("page");
                int size = environment.getArgument("size");
                String name = environment.getArgument("name");

                // 执行查询, 这里随便用一些测试数据来说明问题
                List<User> list = new ArrayList<>(size);
                for (int i = 0; i < size; i++) {
                    User user = new User();
                    user.setId(i);
                    user.setAge(i + 15);
                    user.setSex(i % 2);
                    user.setName(name + "_" + page + "_" + i);
                    user.setPic("pic_" + i + ".jpg");
                    list.add(user);
                }
                return list;
            })
            .build();
    }
}
