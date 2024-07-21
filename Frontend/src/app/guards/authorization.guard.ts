import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const authorizationGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.roles.includes("ADMIN")) {
    return true;
  } else {
    router.navigate(['/admin/notAuthorized']);
    return false;
  }
};
