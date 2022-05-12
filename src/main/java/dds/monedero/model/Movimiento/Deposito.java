package dds.monedero.model.Movimiento;

import dds.monedero.model.Cuenta;

public class Deposito implements TipoMovimiento {
  private Operacion operacion;
  public Deposito() {
    this.operacion = Operacion.DEPOSITO;
  }
  public void impactarEnCuenta(Cuenta cuenta, double monto) {
    cuenta.chequearMontoExtraccionDisponible(monto);
    cuenta.chequearLimiteExtraccionDiario(monto);
    cuenta.depositarSaldo(monto);
  }

  public Operacion getOperacion() {
    return operacion;
  }

}