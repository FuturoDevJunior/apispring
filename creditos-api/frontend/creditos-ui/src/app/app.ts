import { Component } from '@angular/core';
import { ConsultaCreditosComponent } from './components/consulta-creditos/consulta-creditos.component';

@Component({
  selector: 'app-root',
  imports: [ConsultaCreditosComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'Sistema de Consulta de Créditos';
}
