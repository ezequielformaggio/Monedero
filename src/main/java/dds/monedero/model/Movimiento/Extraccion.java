package dds.monedero.model.Movimiento;

import dds.monedero.model.Cuenta;

public class Extraccion implements TipoMovimiento {
  private Operacion operacion;

  public Extraccion() {
    this.operacion = Operacion.EXTRACCION;
  }

  public void impactarEnCuenta(Cuenta cuenta, double monto) {
    cuenta.chequearMontoExtraccionDisponible(monto);
    cuenta.chequearLimiteExtraccionDiario(monto);
    cuenta.extraerSaldo(monto);
  }

  public Operacion getOperacion() {
    return operacion;
  }
}
