/**
 * Pr&aacute;ctricas de Sistemas Inform&aacute;ticos II
 * VisaCancelacionJMSBean.java
 */

package ssii2.visa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.ejb.ActivationConfigProperty;
import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.jms.JMSException;
import javax.annotation.Resource;
import java.util.logging.Logger;

/**
 * @author jaime
 */
@MessageDriven(mappedName = "jms/VisaPagosQueue")
public class VisaCancelacionJMSBean extends DBTester implements MessageListener {
  static final Logger logger = Logger.getLogger("VisaCancelacionJMSBean");
  @Resource
  private MessageDrivenContext mdc;

  private static final String UPDATE_CANCELA_QRY ="UPDATE pago SET codRespuesta=999 "+
  "where idAutorizacion=?";
   // TODO : Definir UPDATE sobre la tabla pagos para poner
   // codRespuesta a 999 dado un código de autorización
   private static final String UPDATE_SALDO_QRY ="UPDATE tarjeta SET saldo = saldo + pago.importe "+
   "FROM pago WHERE idAutorizacion= ? AND tarjeta.numerotarjeta = pago.numerotarjeta";


  public VisaCancelacionJMSBean() {
  }

  // TODO : Método onMessage de ejemplo
  // Modificarlo para ejecutar el UPDATE definido más arriba,
  // asignando el idAutorizacion a lo recibido por el mensaje
  // Para ello conecte a la BD, prepareStatement() y ejecute correctamente
  // la actualización
  public void onMessage(Message inMessage) {
      TextMessage msg = null;
      PreparedStatement pstmt = null;
      Connection con = null;
      String upd;

      try {
          /*Conexion con la DB*/
          con = getConnection();
          upd = UPDATE_CANCELA_QRY;
          pstmt = con.prepareStatement(upd);


          if (inMessage instanceof TextMessage) {
              msg = (TextMessage) inMessage;
              logger.info("MESSAGE BEAN: Message received: " + msg.getText());
              pstmt.setInt(1, Integer.parseInt(msg.getText()));
              pstmt.execute();

              upd = UPDATE_SALDO_QRY;
              pstmt = con.prepareStatement(upd);
              pstmt.setInt(1, Integer.parseInt(msg.getText()));
              logger.info(pstmt.toString());
              pstmt.execute();

          } else {
              logger.warning(
                      "Message of wrong type: "
                      + inMessage.getClass().getName());
          }
      } catch (JMSException e) {
          e.printStackTrace();
          mdc.setRollbackOnly();
      } catch (Throwable te) {
          te.printStackTrace();
      } finally {
            try {
                if (pstmt != null) {
                    pstmt.close(); pstmt = null;
                }
                if (con != null) {
                    closeConnection(con); con = null;
                }
            } catch (SQLException e) {
            }
        }
  }


}
