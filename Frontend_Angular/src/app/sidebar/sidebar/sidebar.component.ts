import { Component, OnInit } from '@angular/core';

export interface RouteInfo {
    path: string;
    title: string;
    icon: string;
    class: string;
    userType: boolean;
}

export const ROUTES: RouteInfo[] = [
    { path: '/dashboard',     title: 'Dashboard',         icon:'nc-bank',       class: 'text-dark', userType: true},
    { path: '/potential-attributes',     title: 'Assesements',         icon:'nc-paper',       class: 'text-dark' , userType: true},
    { path: '/assess-my-potential',         title: 'assess my Potential',             icon:'nc-user-run',    class: 'text-dark' , userType: true},
    { path: '/assess-my-team',         title: 'assess my Team',             icon:'nc-zoom-split',    class: 'text-dark' , userType: true},

    //{ path: '/user',          title: 'Assessment Reports',      icon:'nc-single-copy-04',  class: ''  , userType: true},
    { path: '/critical-roles-home', title: 'Assess Critical Roles',     icon:'nc-bulb-63',    class: '' , userType: true },    
    { path: '/skills-view',         title: 'Assess Critical Skills',        icon:'nc-diamond',    class: '' , userType: true },
    { path: '/mvps',          title:`MVP'S`,      icon:'nc-money-coins',  class: '' , userType: true },
    { path: '/HIPOs',          title:`HIPOs`,      icon:'nc-money-coins',  class: '' , userType: true },
    
    { path: '/talent-mapping',    title: 'Talent Mapping',        icon:'nc-caps-small', class: ''  , userType: true},
    { path: '/succession-plan',    title: 'Succession Plans',        icon:'nc-bulb-63', class: '' , userType: true },
    { path: '/profiles',          title: 'My Profile',      icon:'nc-single-02',  class: ''  , userType: true}, 
    { path: '/MyTeamsProfile',          title: 'My Teams Profiles',     icon: 'nc-badge',  class: ''  , userType: true},  
    { path: '/departments',          title: 'Department',     icon: 'nc-bank',  class: ''  , userType: true},  

];

export const ROUTES_EMPLOYEE: RouteInfo[] = [
    { path: '/dashboard',     title: 'Dashboard',         icon:'nc-bank',       class: 'text-dark', userType: true},
    { path: '/potential-attributes',     title: 'Assesements',         icon:'nc-paper',       class: 'text-dark' , userType: true},

    { path: '/assess-my-potential',         title: 'assess my Potential',             icon:'nc-diamond',    class: 'text-dark' , userType: true},
    { path: '/user',          title: 'Profiles',      icon:'nc-single-02',  class: ''  , userType: true},
    // { path: '/mvps',          title:`MVP'S`,      icon:'nc-money-coins',  class: '' , userType: true },   
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
    public isSidebarVisible: boolean = true; // Flag to toggle visibility of hidden sidebar content

    ngOnInit() {
        this.systemUser = JSON.parse(localStorage.getItem("user"));

        if ((this.systemUser.user.role.id == 1) && ( this.systemUser.user.role.roleName === 'TopManager')) {
            this.menuItems = ROUTES.filter(menuItem => menuItem);
        } else {
            this.menuItems = ROUTES_EMPLOYEE.filter(menuItem => menuItem);
        }
    }

    toggleSidebarVisibility() {
        this.isSidebarVisible = !this.isSidebarVisible;
    }
}
