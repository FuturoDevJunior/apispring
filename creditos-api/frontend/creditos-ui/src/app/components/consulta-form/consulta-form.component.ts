import { Component, EventEmitter, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

export interface ConsultaFormData {
  tipoConsulta: 'nfse' | 'credito';
  valor: string;
}

@Component({
  selector: 'app-consulta-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatIconModule,
    MatProgressSpinnerModule
  ],
  template: `
    <mat-card class="consulta-card">
      <mat-card-header>
        <mat-card-title>
          <mat-icon>search</mat-icon>
          Consulta de Créditos
        </mat-card-title>
        <mat-card-subtitle>
          Pesquise por número da NFSe ou número do crédito
        </mat-card-subtitle>
      </mat-card-header>

      <mat-card-content>
        <form [formGroup]="consultaForm" (ngSubmit)="onSubmit()" class="consulta-form">
          <mat-form-field appearance="outline" class="full-width">
            <mat-label>Tipo de Consulta</mat-label>
            <mat-select formControlName="tipoConsulta" required>
              <mat-option value="nfse">Número da NFSe</mat-option>
              <mat-option value="credito">Número do Crédito</mat-option>
            </mat-select>
            <mat-icon matSuffix>arrow_drop_down</mat-icon>
            <mat-error *ngIf="consultaForm.get('tipoConsulta')?.hasError('required')">
              Selecione o tipo de consulta
            </mat-error>
          </mat-form-field>

          <mat-form-field appearance="outline" class="full-width">
            <mat-label>
              {{ consultaForm.get('tipoConsulta')?.value === 'nfse' ? 'Número da NFSe' : 'Número do Crédito' }}
            </mat-label>
            <input 
              matInput 
              formControlName="valor"
              [placeholder]="getPlaceholder()"
              maxlength="50"
              required>
            <mat-icon matSuffix>{{ getIcon() }}</mat-icon>
            <mat-hint align="end">
              {{ consultaForm.get('valor')?.value?.length || 0 }}/50
            </mat-hint>
            <mat-error *ngIf="consultaForm.get('valor')?.hasError('required')">
              {{ getErrorMessage() }}
            </mat-error>
            <mat-error *ngIf="consultaForm.get('valor')?.hasError('pattern')">
              Formato inválido. Use apenas números e letras.
            </mat-error>
          </mat-form-field>

          <div class="form-actions">
            <button 
              mat-raised-button 
              color="primary" 
              type="submit"
              [disabled]="consultaForm.invalid || carregando"
              class="consultar-btn">
              <mat-spinner 
                *ngIf="carregando" 
                diameter="20" 
                class="spinner">
              </mat-spinner>
              <mat-icon *ngIf="!carregando">search</mat-icon>
              {{ carregando ? 'Consultando...' : 'Consultar' }}
            </button>

            <button 
              mat-button 
              type="button"
              (click)="limparForm()"
              [disabled]="carregando"
              class="limpar-btn">
              <mat-icon>clear</mat-icon>
              Limpar
            </button>
          </div>
        </form>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    .consulta-card {
      max-width: 600px;
      margin: 20px auto;
      box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    }

    mat-card-header {
      margin-bottom: 16px;
    }

    mat-card-title {
      display: flex;
      align-items: center;
      gap: 8px;
      color: #1976d2;
    }

    .consulta-form {
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .full-width {
      width: 100%;
    }

    .form-actions {
      display: flex;
      gap: 12px;
      justify-content: flex-start;
      margin-top: 8px;
    }

    .consultar-btn {
      flex: 1;
      min-width: 140px;
      height: 44px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 8px;
    }

    .limpar-btn {
      min-width: 100px;
      height: 44px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 4px;
    }

    .spinner {
      margin-right: 8px;
    }

    /* Responsividade */
    @media (max-width: 768px) {
      .consulta-card {
        margin: 10px;
        max-width: none;
      }

      .form-actions {
        flex-direction: column;
      }

      .consultar-btn,
      .limpar-btn {
        width: 100%;
      }
    }
  `]
})
export class ConsultaFormComponent {
  @Output() consultar = new EventEmitter<ConsultaFormData>();
  
  consultaForm: FormGroup;
  carregando = false;

  constructor(private fb: FormBuilder) {
    this.consultaForm = this.fb.group({
      tipoConsulta: ['nfse', [Validators.required]],
      valor: ['', [Validators.required, Validators.pattern(/^[a-zA-Z0-9\-\.\/]+$/)]]
    });
  }

  onSubmit(): void {
    if (this.consultaForm.valid) {
      const formData: ConsultaFormData = {
        tipoConsulta: this.consultaForm.get('tipoConsulta')?.value,
        valor: this.consultaForm.get('valor')?.value.trim()
      };
      
      this.setCarregando(true);
      this.consultar.emit(formData);
    }
  }

  limparForm(): void {
    this.consultaForm.reset({
      tipoConsulta: 'nfse',
      valor: ''
    });
  }

  setCarregando(estado: boolean): void {
    this.carregando = estado;
  }

  getPlaceholder(): string {
    const tipo = this.consultaForm.get('tipoConsulta')?.value;
    return tipo === 'nfse' 
      ? 'Ex: 123456789' 
      : 'Ex: CRD-2024-001';
  }

  getIcon(): string {
    const tipo = this.consultaForm.get('tipoConsulta')?.value;
    return tipo === 'nfse' ? 'receipt' : 'account_balance';
  }

  getErrorMessage(): string {
    const tipo = this.consultaForm.get('tipoConsulta')?.value;
    return tipo === 'nfse' 
      ? 'Informe o número da NFSe' 
      : 'Informe o número do crédito';
  }
}
