package transport

import (
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/model"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func FilterFields(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Lấy query parameters
		location := c.Query("location")
		fieldTypeStr := c.Query("type")
		minPriceStr := c.Query("min_price")
		maxPriceStr := c.Query("max_price")

		// Chuyển đổi giá trị số
		minPrice, _ := strconv.Atoi(minPriceStr)
		maxPrice, _ := strconv.Atoi(maxPriceStr)

		// Chuyển đổi type field
		var fieldType *model.TypeField
		if fieldTypeStr != "" {
			parsedType, err := model.ParseStrTypeField(fieldTypeStr)
			if err == nil {
				fieldType = &parsedType
			}
		}

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewFilterFieldsBiz(fieldStorage)

		// Lọc danh sách field
		fields, err := fieldBiz.FilterFields(context.Background(), location, fieldType, minPrice, maxPrice)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		// Trả về JSON
		c.JSON(http.StatusOK, gin.H{"data": fields})
	}
}
