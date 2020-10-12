/*
 *    Copyright 2020 Maksym Sanzharov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package io.github.maksymsan.persistentwebapp1.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class NamedObject {

    @Id
    @Column(nullable = false, length = 50)
    private String primaryKey;
    @Column(length = 50)
    private String name;
    @Column(length = 100)
    private String description;
    private LocalDateTime updatedTimestamp;

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Override
    public String toString() {
        return "NamedObject{" +
                "primaryKey='" + primaryKey + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamedObject)) return false;
        NamedObject that = (NamedObject) o;
        return primaryKey.equals(that.primaryKey) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(updatedTimestamp, that.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryKey, name, description, updatedTimestamp);
    }
}
