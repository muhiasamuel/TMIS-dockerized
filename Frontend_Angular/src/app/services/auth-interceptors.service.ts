import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { Observable } from "rxjs";
import { HttpServiceService } from "./http-service.service";

@Injectable()
export class AuthInterceptorsService implements HttpInterceptor {

  // constructor(private httpService: HttpServiceService) {}
  httpService:HttpServiceService = inject(HttpServiceService)

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const systemUser = localStorage.getItem('user');
    if (systemUser) {
      const userData = JSON.parse(systemUser);
      const authToken = userData.authToken;
      console.log('Token:', authToken);
      const modifiedReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${authToken}`
        }
      });
      console.log('Modified Request:', modifiedReq);
      return next.handle(modifiedReq);
    } else {
      return next.handle(req);
    }
  }
}
