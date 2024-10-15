import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

export interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
    userType: boolean;
    requiredPermissions: string[];  // Add this field to define the required permissions for each route
}

export const ROUTES: RouteInfo[] = [
    { path: '/dashboard', title: 'Dashboard', icon: 'nc-bank', class: 'text-dark', userType: true, requiredPermissions: [] },
    { path: '/potential-attributes', title: 'Assessments', icon: 'nc-paper', class: 'text-dark', userType: true, requiredPermissions: [] },
    { path: '/assess-my-potential', title: 'Assess My Potential', icon: 'nc-user-run', class: 'text-dark', userType: true, requiredPermissions: [] },
    { path: '/assess-my-team', title: 'Assess My Team', icon: 'nc-zoom-split', class: 'text-dark', userType: true, requiredPermissions: [] },
    { path: '/critical-roles-home', title: 'Assess Critical Roles', icon: 'nc-bulb-63', class: '', userType: true, requiredPermissions: [] },
    { path: '/skills-view', title: 'Assess Critical Skills', icon: 'nc-diamond', class: '', userType: true, requiredPermissions: [] },
    { path: '/mvps', title: 'MVPs', icon: 'nc-money-coins', class: '', userType: true, requiredPermissions: [] },
    { path: '/HIPOs', title: 'HIPOs', icon: 'nc-money-coins', class: '', userType: true, requiredPermissions: [] },
    { path: '/talent-mapping', title: 'Talent Mapping', icon: 'nc-caps-small', class: '', userType: true, requiredPermissions: [] },
    { path: '/succession-plan', title: 'Succession Plans', icon: 'nc-bulb-63', class: '', userType: true, requiredPermissions: [] },
    { path: '/profiles', title: 'My Profile', icon: 'nc-single-02', class: '', userType: true, requiredPermissions: ['VIEW_OWN_PROFILE'] },
    { path: '/MyTeamsProfile', title: 'My Teams Profiles', icon: 'nc-badge', class: '', userType: true, requiredPermissions: [] },
    { path: '/departments', title: 'Departments', icon: 'nc-bank', class: '', userType: true, requiredPermissions: [] }
];

@Component({
    moduleId: module.id,
    selector: 'app-sidebar',
    templateUrl: 'sidebar.component.html',
    styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent implements OnInit {
    systemUser: any;
    public menuItems: RouteInfo[] = [];
    public isSidebarVisible: boolean = true;

    constructor(private router: Router) {}

    ngOnInit() {
        this.systemUser = JSON.parse(sessionStorage.getItem("user"));

        if (this.systemUser) {
            const userPermissions = this.systemUser.permissions || [];
            this.menuItems = ROUTES.filter(menuItem => 
                menuItem.requiredPermissions.length === 0 ||  // No restrictions
                menuItem.requiredPermissions.every(permission => 
                    userPermissions.some(userPerm => userPerm.permissionName === permission)
                )
            );
        }
    }

    logout() {
        sessionStorage.clear();
        this.router.navigate(['/']);
    }

    toggleSidebarVisibility() {
        this.isSidebarVisible = !this.isSidebarVisible;
    }
}
