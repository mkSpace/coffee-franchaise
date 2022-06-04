package extensions

import javax.swing.JLabel
import javax.swing.SwingConstants

fun JLabel.setAlignmentCenter() {
    horizontalAlignment = SwingConstants.CENTER
    verticalAlignment = SwingConstants.CENTER
}