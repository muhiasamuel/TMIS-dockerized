import { Injectable } from '@angular/core';
import { CanActivate, Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class PermissionGuard implements CanActivate {
  constructor(private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const systemUser = localStorage.getItem('user');
    
    if (systemUser) {
      const user = JSON.parse(systemUser);
      const requiredPermissions = route.data['permissions'] as Array<string>;

      if (this.hasPermissions(user.permissions, requiredPermissions)) {
        return true;
      } else {
        // Redirect to a "no access" page or another route
        alert("Access Denied. You Do Not Have The Authority To Access The Page")
        return false;
      }
    }

    // If no user is logged in, redirect to login
    this.router.navigate(['/']);
    return false;
  }

  private hasPermissions(userPermissions: Array<{ permissionName: string }>, requiredPermissions: Array<string>): boolean {
    // Check if the user has the required permission(s) by matching permissionName
    return requiredPermissions.every(requiredPermission =>
      userPermissions.some(permission => permission.permissionName === requiredPermission)
    );
  }
}
