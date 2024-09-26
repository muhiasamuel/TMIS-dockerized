import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private router: Router) {}

  canActivate(): boolean {
    const systemUser = localStorage.getItem('user');
    if (systemUser) {
      return true;
    } else {
      this.router.navigate(['/']);  // Redirect to login if not authenticated
      return false;
    }
  }

  canActivateChild(): boolean {
    return this.canActivate(); // Same logic for child routes
  }
}
