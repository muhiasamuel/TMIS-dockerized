import { Component, OnInit } from '@angular/core';

@Component({
  moduleId: module.id,
  selector: 'app-plugin',
  templateUrl: './plugin.component.html',
  styleUrl: './plugin.component.scss'
})
export class PluginComponent implements OnInit{

  public sidebarColor: string = "white";
  public sidebarActiveColor: string = "danger";

  public state: boolean = true;

  changeSidebarColor(color:any){
    var sidebar = <HTMLElement>document.querySelector('.sidebar');

    this.sidebarColor = color;
    if(sidebar != undefined){
        sidebar.setAttribute('data-color',color);
    }
  }
  changeSidebarActiveColor(color:any){
    var sidebar = <HTMLElement>document.querySelector('.sidebar');
    this.sidebarActiveColor = color;
    if(sidebar != undefined){
        sidebar.setAttribute('data-active-color',color);
    }
  }

  ngOnInit(): void {
    
  }
}
