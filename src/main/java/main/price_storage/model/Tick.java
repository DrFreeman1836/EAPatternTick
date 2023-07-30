package main.price_storage.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TICK")
public class Tick {

  /**
   * id тика
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  /**
   * цена Ask
   */
  @Column(columnDefinition = "decimal(7,5)", name = "ASK")
  private BigDecimal ask;

  /**
   * цена Bid
   */
  @Column(columnDefinition = "decimal(7,5)", name = "BID")
  private BigDecimal bid;

//  /**
//   * время тика в мс(на входе в контроллер)
//   */
//  @Column(name = "TIMESTAMP")
//  private Long timestamp;

  /**
   * Время последнего обновления цен
   */
  @Column(name = "TIME")
  private Date time;

  /**
   * Время последнего обновления цен в миллисекундах(от МТ)
   */
  @Column(name = "TIME_MSC")
  private Long timeMsc;

  /**
   * Текущая цена последней сделки (Last)
   */
  @Column(name = "LAST", columnDefinition = "decimal(7,5)")
  private BigDecimal last;

  /**
   * Объем для текущей цены Last
   */
  @Column(name = "VOLUME")
  private Long volume;

  /**
   * Объем для текущей цены Last c повышенной точностью
   */
  @Column(name = "VOLUME_REAL")
  private Double volumeReal;

  /**
   * Флаги тиков(пока не понятно как их считать)
   */
  @Column(name = "FLAGS")
  private Integer flags;

  @Override
  public String toString() {
    return "Tick{" +
        "id=" + id +
        ", priceAsk=" + ask +
        ", priceBid=" + bid +
        //", timestamp=" + timestamp +
        '}';
  }
}
