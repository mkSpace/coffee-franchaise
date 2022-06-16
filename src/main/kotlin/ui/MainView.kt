package ui

import db.DBConnection
import db.entity.*
import db.repository.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class MainView(
    private val memberRepository: MemberRepository,
    private val productRepository: ProductRepository,
    private val baristaRepository: BaristaRepository,
    private val orderRepository: OrderRepository,
    private val provideRepository: ProvideRepository
) : View {

    override fun setupViews() {
        do {
            val menu = showMenu();
            when (menu) {
                1 -> runUserManual()
                2 -> runStoreManual()
            }
        } while (menu != 3)
    }

    private fun showMenu(): Int {
        println("---------- Jaemin's Cafe Menual ------------")
        println("1: 사용자 매뉴얼")
        println("2: 카페 매뉴얼")
        println("3: 종료")
        print("> ")
        return Scanner(System.`in`).nextInt()
    }

    private fun runUserManual() {
        do {
            val menu = showUserMenu()
            when (menu) {
                1 -> runSignUp()
                2 -> runOrderManual()
                3 -> showUser()
            }
        } while (menu != 4)
    }

    private fun showUserMenu(): Int {
        println("---------- Use Manual ------------")
        println("1: 회원가입")
        println("2: 주문")
        println("3: 내 정보")
        println("4: 메인메뉴로 가기")
        print("> ")
        return Scanner(System.`in`).nextInt()
    }

    private fun runSignUp() {
        println("---------- Sign Up ------------")
        print("id : ")
        val scanner = Scanner(System.`in`)
        val memberId = scanner.next()
        print("password: ")
        val password = scanner.next()
        print("name: ")
        val name = scanner.next()
        print("age: ")
        val age = scanner.nextInt()
        print("job: ")
        val job = scanner.next()
        val member = Member(memberId, password, name, age, job, 0)
        val saveMember = memberRepository.save(member)
        println("안녕하세요. ${saveMember.name}님. 회원가입이 완료되었습니다.")
    }

    private fun runOrderManual() {
        do {
            val menu = showOrderMenu()
            when (menu) {
                1 -> showProductList()
                2 -> showAllBaristas()
                3 -> order()
            }
        } while (menu != 4)
    }

    private fun showOrderMenu(): Int {
        println("---------- Order Manual ------------")
        println("1: 상품 보기")
        println("2: 바리스타 보기")
        println("3: 주문하기")
        println("4: 메인메뉴로 가기")
        print("> ")
        return Scanner(System.`in`).nextInt()
    }

    private fun showProductList() {
        println("Product 정보 --------------------------")
        productRepository.findAll().forEach { product ->
            println("[${product.id}] ${product.name}상품이 ${product.price}원으로 ${product.stockCount}개 남았습니다.")
        }
    }

    private fun showAllBaristas() {
        println("Barista 정보 --------------------------")
        baristaRepository.findAll().forEach { barista ->
            println("[${barista.name}] ${barista.name} 님이 ${barista.entryAt} 에 입사하셨습니다.")
        }
    }

    private fun order() {
        println("Order -------------------------")
        println("계정이 있는 회원만 주문할 수 있어요! 로그인 해 주세요!")
        val member = runSignIn()
        if (member == null) {
            println("등록되지 않은 회원이에요! 가입부터 해주세요!")
            return
        }
        val scanner = Scanner(System.`in`)
        print("주문상품 id : ")
        val productId = scanner.nextInt()
        val product = productRepository.findById(productId)
        print("몇 개 주문하시겠요? : ")
        val orderCount = scanner.nextInt()
        print("선택할 바리스타명 : ")
        if (product.stockCount - orderCount < 0) {
            println("현재 그 만큼의 재고가 준비되어있지 않아요! 다시 주문해주세요!")
            return
        }
        val baristaName = scanner.next()
        val barista = baristaRepository.findByName(baristaName)
        val canUseCoupon = if (member.couponCount > 0) member.couponCount / 10 else 0
        var usingCoupon = 0
        if (canUseCoupon > 0) {
            println("쿠폰을 사용해 주문할 수 있어요! 멏 개 사용하실래요? ($canUseCoupon 개 사용가능) : ")
            usingCoupon = scanner.nextInt()
        }
        println("결제방식(1: 카드, 2: 현금, 3: 쿠폰[일부 사용가능]) : ")
        val method = scanner.nextInt().let {
            when (it) {
                2 -> Method.CASH
                3 -> Method.COUPON
                else -> Method.CASH
            }
        }
        val order = Order.create(method, orderCount, member, product, usingCoupon)
        val provide = Provide.create(order.totalPrice, order.id, barista.name)
        if (usingCoupon > 0) {
            memberRepository.updateCouponCount(member.memberId, (member.couponCount - usingCoupon * 10) + (orderCount - usingCoupon))
        } else {
            memberRepository.updateCouponCount(member.memberId, member.couponCount + orderCount)
        }
        productRepository.updateStockCount(productId, product.stockCount - orderCount)
        orderRepository.save(order)
        provideRepository.save(provide)
        println("[${order.id}] ${member.name} 님, ${product.name} 상품 $orderCount 개가 $baristaName 바리스타 에게 주문이 완료되었습니다.")
    }

    private fun showUser() {
        println("-------- My Info -------")
        val member = runSignIn()
        if (member == null) {
            println("등록되지 않은 회원이에요! 가입부터 해주세요!")
            return
        }
        println("[${member.memberId}] ${member.name}님, 나이는 ${member.age} 이며, ${member.job} 이시군요! 쿠폰은 총 ${member.couponCount}개 적립되셨습니다.")
    }

    private fun runSignIn(): Member? {
        println("----------- Sign In ------------")
        val scanner = Scanner(System.`in`)
        print("Id : ")
        val id = scanner.next()
        print("password: ")
        val password = scanner.next()
        return memberRepository.findByIdAndPassword(id, password)
    }

    private fun runStoreManual() {
        do {
            val menu = showStoreMenu()
            when (menu) {
                1 -> hireBarista()
                2 -> fireBarista()
                3 -> showProductList()
                4 -> addProduct()
                5 -> removeProduct()
                6 -> showAllOrders()
                7 -> cancelOrder()
                8 -> calculate()
            }
        } while (menu != 9)
    }

    private fun showStoreMenu(): Int {
        println("---------- Use Manual ------------")
        println("1: 바리스타 고용")
        println("2: 바리스타 해고")
        println("3: 상품 확인")
        println("4: 상품 추가")
        println("5: 상품 제거")
        println("6: 주문 확인")
        println("7: 주문 취소")
        println("8: 정산하기")
        println("9: 메인메뉴로 돌아가기")
        print("> ")
        return Scanner(System.`in`).nextInt()
    }

    private fun hireBarista() {
        println("---------- Hire Barista ------------")
        print("baristaName : ")
        val scanner = Scanner(System.`in`)
        val baristaName = scanner.next()
        print("입사일(yyyy/MM/dd): ")
        val entryAt = LocalDate.parse(scanner.next(), DateTimeFormatter.ofPattern("yyyy/MM/dd"))
        print("phoneNumber: ")
        val phoneNumber = scanner.next()
        val barista = Barista(baristaName, entryAt, phoneNumber)
        val saveBarista = baristaRepository.save(barista)
        println("안녕하세요. ${saveBarista.name}님. 함께 일하게 되어 기뻐요!!")
    }

    private fun fireBarista() {
        println("---------- Hire Barista ------------")
        showAllBaristas()
        print("baristaName : ")
        val scanner = Scanner(System.`in`)
        val baristaName = scanner.next()
        baristaRepository.delete(baristaName)
        println("안녕히가세요. ${baristaName}님. 함께 일할 수 있어 행복했어요!!")
    }

    private fun addProduct() {
        println("---------- Add Product ------------")
        val scanner = Scanner(System.`in`)
        val productId = DBConnection.getLatestProductId() + 1
        print("상품명 : ")
        val productName = scanner.next()
        print("재고량 : ")
        val stockCount = scanner.nextInt()
        print("단가 : ")
        val price = scanner.nextInt()
        val product = Product(productId, productName, stockCount, price)
        val saveProduct = productRepository.save(product)
        println("${saveProduct.name} 상품이 ${saveProduct.price}의 가격으로 ${saveProduct.stockCount}개 입고되었습니다.")
    }

    private fun removeProduct() {
        println("----------- Remove Product -----------")
        println("Product 정보 --------------------------")
        productRepository.findAll().forEach { product ->
            println("[${product.id}] ${product.name}상품이 ${product.price}원으로 ${product.stockCount}개 남았습니다.")
        }
        print("Delete Product Id > ")
        productRepository.delete(Scanner(System.`in`).nextInt())
        println("성공적으로 삭제하였습니다.")
    }

    private fun showAllOrders() {
        println("Order 정보 --------------------------")
        orderRepository.findAll().forEach { order ->
            val product = productRepository.findById(order.orderProduct)
            println("[${order.id}] ${product.name} 상품이 ${order.orderCount}개 준비되고 있습니다.")
        }
    }

    private fun cancelOrder() {
        println("---------- Cancel Order ---------")
        print("취소할 Order Id : ")
        val cancelOrderId = Scanner(System.`in`).nextInt()
        val order = orderRepository.findById(cancelOrderId)
        val member = memberRepository.findById(order.orderMember)
        val product = productRepository.findById(order.orderProduct)
        memberRepository.updateCouponCount(member.memberId, member.couponCount + order.couponCount * 10)
        productRepository.updateStockCount(product.id, product.stockCount + order.orderCount)
        val provide = provideRepository.findByOrder(order.id)
        provideRepository.delete(provide.id)
        orderRepository.delete(order.id)
        println("[${order.id}] 주문이 성공적으로 취소되었습니다.")
    }

    private fun calculate() {
        provideRepository.findAll().groupBy { it.baristaName }.forEach {
            val (baristaName, provides) = it
            val sumOfProvidePrice = provides.sumOf { it.totalPrice }
            println("[$baristaName] 은 총 $sumOfProvidePrice 금액의 주문을 처리하셨고, 정산받을 금액은 ${sumOfProvidePrice / 10} 원 입니다.")
        }
        val sellingPrice = orderRepository.findAll().sumOf { it.totalPrice }
        println("Jaemin's Cafe의 총 매출액은 $sellingPrice 입니다.")
    }
}