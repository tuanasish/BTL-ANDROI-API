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

func UpdateField(db *gorm.DB) gin.HandlerFunc {
	return func(c *gin.Context) {
		// Lấy id từ param
		id, err := strconv.Atoi(c.Param("id"))
		if err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": "ID không hợp lệ"})
			return
		}

		var data model.FieldUpdate

		// Kiểm tra dữ liệu đầu vào
		if err := c.ShouldBindJSON(&data); err != nil {
			c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
			return
		}

		// Khởi tạo storage và biz
		fieldStorage := storage.NewStorageField(db)
		fieldBiz := biz.NewUpdateFieldBiz(fieldStorage)

		// Thực hiện cập nhật
		if err := fieldBiz.UpdateField(context.Background(), id, &data); err != nil {
			c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
			return
		}

		c.JSON(http.StatusOK, gin.H{"message": "Field updated successfully"})
	}
}
