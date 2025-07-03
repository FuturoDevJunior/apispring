import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ConsultaCreditosComponent } from './components/consulta-creditos/consulta-creditos.component';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, ConsultaCreditosComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  title = 'Sistema de Consulta de Créditos';
}
