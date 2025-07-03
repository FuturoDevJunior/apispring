import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface CreditoDTO {
  id: number;
  numeroNfse: string;
  numeroCredito: string;
  cnpjPrestador: string;
  razaoSocialPrestador: string;
  dataVencimento: string;
  valor: number;
  situacao: string;
  dataConsulta: string;
}

export interface ConsultaResponse {
  creditos: CreditoDTO[];
  total: number;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class ConsultaService {
  private readonly baseUrl = 'http://localhost:8080/api/creditos';

  private http = inject(HttpClient);

  /**
   * Consulta créditos por número da NFSe
   * @param numeroNfse Número da NFSe para consulta
   * @returns Observable com a lista de créditos encontrados
   */
  consultarPorNfse(numeroNfse: string): Observable<ConsultaResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Correlation-ID': this.generateCorrelationId()
    });

    return this.http.get<CreditoDTO[]>(`${this.baseUrl}/${numeroNfse}`, { headers })
      .pipe(
        map(creditos => ({
          creditos,
          total: creditos.length,
          timestamp: new Date().toISOString()
        }))
      );
  }

  /**
   * Consulta crédito específico por número do crédito
   * @param numeroCredito Número do crédito para consulta
   * @returns Observable com o crédito encontrado
   */
  consultarPorCredito(numeroCredito: string): Observable<ConsultaResponse> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Correlation-ID': this.generateCorrelationId()
    });

    return this.http.get<CreditoDTO>(`${this.baseUrl}/credito/${numeroCredito}`, { headers })
      .pipe(
        map(credito => ({
          creditos: [credito],
          total: 1,
          timestamp: new Date().toISOString()
        }))
      );
  }

  /**
   * Gera um ID de correlação único para rastreamento
   * @returns String UUID v4
   */
  private generateCorrelationId(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      const r = Math.random() * 16 | 0;
      const v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }
}
