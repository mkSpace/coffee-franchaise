package extensions

import javax.swing.JOptionPane

object Message {
    fun showAlertMessage(message: String) {
        JOptionPane.showMessageDialog(null, message, "알림", JOptionPane.INFORMATION_MESSAGE)
    }

    fun showErrorMessage(message: String) {
        JOptionPane.showMessageDialog(null, message, "에러", JOptionPane.ERROR_MESSAGE)
    }
}