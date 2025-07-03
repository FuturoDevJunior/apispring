import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCardModule } from '@angular/material/card';
import { MatDividerModule } from '@angular/material/divider';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTableModule } from '@angular/material/table';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { HttpClientModule } from '@angular/common/http';
import { map } from 'rxjs/operators';

import { Credito } from '../../models/credito.model';
import { CreditoService } from '../../services/credito.service';
import { ConsultaFormularioComponent, ConsultaFormData } from '../consulta-formulario/consulta-formulario.component';

@Component({
  selector: 'app-consulta-creditos',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatDividerModule,
    MatIconModule,
    MatToolbarModule,
    MatTableModule,
    MatProgressSpinnerModule,
    HttpClientModule,
    ConsultaFormularioComponent
  ],
  template: `
    <div class="consulta-container">
      <mat-toolbar color="primary" class="header-toolbar">
        <mat-icon>account_balance</mat-icon>
        <span>Sistema de Consulta de Créditos</span>
        <span class="spacer"></span>
        <mat-icon>help_outline</mat-icon>
      </mat-toolbar>
      
      <div class="content-container">
        <mat-card class="formulario-card">
          <mat-card-header>
            <mat-card-title>
              <mat-icon>search</mat-icon>
              Consultar Créditos
            </mat-card-title>
            <mat-card-subtitle>
              Digite o número da NFS-e ou número do crédito para consultar
            </mat-card-subtitle>
          </mat-card-header>
          
          <mat-card-content>
            <app-consulta-formulario
              (consultar)="onConsultaRealizada($event)">
            </app-consulta-formulario>
          </mat-card-content>
        </mat-card>
        
        <mat-divider *ngIf="mostrarResultados"></mat-divider>
        
        <div *ngIf="mostrarResultados" class="resultados-section">
          <mat-card *ngIf="carregando" class="loading-card">
            <mat-card-content>
              <div class="loading-content">
                <mat-progress-spinner mode="indeterminate" diameter="50"></mat-progress-spinner>
                <p>Carregando...</p>
              </div>
            </mat-card-content>
          </mat-card>
          
          <mat-card *ngIf="erro && !carregando" class="error-card">
            <mat-card-content>
              <div class="error-content">
                <mat-icon color="warn">error</mat-icon>
                <p>{{ erro }}</p>
              </div>
            </mat-card-content>
          </mat-card>
          
          <mat-card *ngIf="creditos.length > 0 && !carregando && !erro" class="results-card">
            <mat-card-header>
              <mat-card-title>Resultados da Consulta</mat-card-title>
              <mat-card-subtitle>{{ creditos.length }} crédito(s) encontrado(s)</mat-card-subtitle>
            </mat-card-header>
            
            <mat-card-content>
              <table mat-table [dataSource]="creditos" class="results-table">
                <ng-container matColumnDef="numeroCredito">
                  <th mat-header-cell *matHeaderCellDef>Número do Crédito</th>
                  <td mat-cell *matCellDef="let credito">{{ credito.numeroCredito }}</td>
                </ng-container>
                
                <ng-container matColumnDef="numeroNfse">
                  <th mat-header-cell *matHeaderCellDef>Número NFS-e</th>
                  <td mat-cell *matCellDef="let credito">{{ credito.numeroNfse }}</td>
                </ng-container>
                
                <ng-container matColumnDef="dataConstituicao">
                  <th mat-header-cell *matHeaderCellDef>Data Constituição</th>
                  <td mat-cell *matCellDef="let credito">{{ credito.dataConstituicao | date: 'dd/MM/yyyy' }}</td>
                </ng-container>
                
                <ng-container matColumnDef="valorIssqn">
                  <th mat-header-cell *matHeaderCellDef>Valor ISSQN</th>
                  <td mat-cell *matCellDef="let credito">{{ credito.valorIssqn | currency: 'BRL' }}</td>
                </ng-container>
                
                <ng-container matColumnDef="tipoCredito">
                  <th mat-header-cell *matHeaderCellDef>Tipo</th>
                  <td mat-cell *matCellDef="let credito">{{ credito.tipoCredito }}</td>
                </ng-container>
                
                <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
                <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
              </table>
            </mat-card-content>
          </mat-card>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./consulta-creditos.component.scss']
})
export class ConsultaCreditosComponent {
  creditos: Credito[] = [];
  carregando = false;
  erro: string | null = null;
  mostrarResultados = false;
  displayedColumns: string[] = ['numeroCredito', 'numeroNfse', 'dataConstituicao', 'valorIssqn', 'tipoCredito'];

  constructor(private creditoService: CreditoService) {}

  onConsultaRealizada(parametros: ConsultaFormData) {
    this.carregando = true;
    this.erro = null;
    this.creditos = [];
    this.mostrarResultados = true;

    if (parametros.tipoConsulta === 'nfse') {
      this.creditoService.buscarPorNumeroNfse(parametros.valor).subscribe({
        next: (creditos: Credito[]) => {
          this.creditos = creditos;
          this.carregando = false;
          
          if (this.creditos.length === 0) {
            this.erro = 'Nenhum crédito encontrado para os parâmetros informados.';
          }
        },
        error: (error: any) => {
          console.error('Erro na consulta:', error);
          this.carregando = false;
          this.erro = error.message || 'Erro inesperado. Tente novamente mais tarde.';
        }
      });
    } else if (parametros.tipoConsulta === 'credito') {
      this.creditoService.buscarPorNumeroCredito(parametros.valor).subscribe({
        next: (credito: Credito) => {
          this.creditos = [credito];
          this.carregando = false;
        },
        error: (error: any) => {
          console.error('Erro na consulta:', error);
          this.carregando = false;
          this.erro = error.message || 'Erro inesperado. Tente novamente mais tarde.';
        }
      });
    } else {
      this.carregando = false;
      this.erro = 'Parâmetros de consulta inválidos';
      return;
    }
  }

}
