import { HeartIcon, HomeIcon, LocationIcon, PhoneIcon } from '../Common/Icons.jsx';
import './Footer.css';
import qrZalo from "../../assets/imgs/qr_zl.png";

function Footer() {
  return (
    <footer className="footer" id="contact">
      <div className="footer__brand">
        <div className="footer__brand-mark">
          <HomeIcon />
        </div>
        <div>
          <h2>TANEzHouse</h2>
          <p>Phòng đẹp giá rẻ</p>
        </div>
      </div>

      <div className="footer__columns">
        <section>
          <h3>Liên kết</h3>
          <a href="#home">Trang chủ</a>
          <a href="#room-list">Tìm phòng</a>
          <a href="#contact">Phòng đã lưu</a>
          <a href="#contact">Liên hệ</a>
        </section>

        <section>
          <h3>Hỗ trợ</h3>
          <a href="#contact">Hướng dẫn tìm phòng</a>
          <a href="#contact">Quy định đăng tin</a>
          <a href="#contact">Chính sách bảo mật</a>
          <a href="#contact">Điều khoản sử dụng</a>
        </section>

        <section className="footer__contact">
          <h3>Liên hệ</h3>
          <div className="footer__qr">
            <img src={qrZalo} alt="QR Zalo" />
          </div>
          <div className="footer__contact-list">
            <p>
              <PhoneIcon /> 0349 909 412
            </p>
            <p>
              <HeartIcon /> tan2005tg@gmail.com
            </p>
            <p>
              <LocationIcon /> 193/8 Đường số 20, Phường 5, Gò Vấp, TP. HCM
            </p>
          </div>
        </section>
      </div>

      <div className="footer__bottom">© 2026 TÂN EzHouse. All rights reserved.</div>
    </footer>
  );
}

export default Footer;