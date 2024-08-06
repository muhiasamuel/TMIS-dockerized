import { inject } from "@angular/core";

export const canActivateChild = () => {
  const systemUser = localStorage.getItem('user');
  
  if(systemUser) {
    return true;
  } else {
    return false;
  }
}