package transport

import (
	"BOOKING_BE/modules/field/biz"
	"BOOKING_BE/modules/field/storage"
	"context"
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"gorm.io/gorm"
)

func GetFieldByID(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Lấy ID từ param
		id, err := strconv.Atoi(c.Param("id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "ID không hợp lệ"})
			return
		}

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewGetFieldBiz(fieldStorage)

		// Lấy thông tin sân
		field, err := fieldBiz.GetFieldByID(context.Background(), id)
		if err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		// Trả về JSON
		c.JSON(http.StatusOK, gin.H{"data": field})
	}
}
