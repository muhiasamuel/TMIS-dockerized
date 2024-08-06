package com.example.talent_man.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RoleResDto{
  private   Long id;
  private   String roleName;
  private Set<PermissionResDto> permissions;

}
