package com.joecos.iam.modules.permission.model;

import com.joecos.iam.infrastructure.persistence.entity.PermissionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PermissionTreeBuilder {
    /**
     * 根据权限实体列表，建立权限树。
     *
     * @param permissions 权限实体列表
     *
     * */
    public List<PermissionTree> buildPermissionTree(List<PermissionEntity> permissions) {

        // 扫描权限实体列表，生成平铺权限列表
        HashMap<Integer, PermissionTree> permissionMap = new HashMap<>();
        for(PermissionEntity permission : permissions) {
            Integer id = permission.getId();
            String name = permission.getPermissionName();
            String code = permission.getPermissionCode();
            String type = permission.getType();

            PermissionTree tree = new PermissionTree();
            tree.setId(id);
            tree.setPermissionName(name);
            tree.setPermissionCode(code);
            tree.setPermissionType(type);

            permissionMap.put(id, tree);
        }

        // 连接每一个权节点存在的父权限节点
        List<PermissionTree> rootTree = new ArrayList<>();
        for(PermissionEntity permission : permissions) {

            Integer parentId = permission.getParentId();
            Integer childId = permission.getId();

            if (parentId != null) {
                PermissionTree parentTree = permissionMap.get(parentId);
                PermissionTree childTree = permissionMap.get(childId);
                parentTree.children.add(childTree);

            } else {
                rootTree.add(permissionMap.get(childId));
            }
        }

        return rootTree;
    }
}
