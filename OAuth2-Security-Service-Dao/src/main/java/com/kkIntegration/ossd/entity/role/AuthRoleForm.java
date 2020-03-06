package com.kkIntegration.ossd.entity.role;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: ckk
 * create: 2020-01-13 15:33
 */
@Data
public class AuthRoleForm {

    String target_id;

    List<String> target_roles = new ArrayList<>();

    String target_type;

}
