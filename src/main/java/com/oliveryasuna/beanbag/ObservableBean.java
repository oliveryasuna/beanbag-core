/*
 * Copyright 2022 Oliver Yasuna
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without
 *      specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.oliveryasuna.beanbag;

import com.oliveryasuna.beanbag.event.BeanChangedEvent;
import com.oliveryasuna.beanbag.listener.BeanChangedListener;
import com.oliveryasuna.commons.language.pattern.registry.Registration;
import org.apache.commons.lang3.event.EventListenerSupport;

public abstract class ObservableBean<T, SUB extends ObservableBean<T, SUB>> {

  // Constructors
  //--------------------------------------------------

  protected ObservableBean(final T bean) {

    super();

    this.bean = bean;
  }

  // Bean field
  //--------------------------------------------------

  protected T bean;

  // Listener registries
  //--------------------------------------------------

  protected final EventListenerSupport<BeanChangedListener> beanChangedListeners = EventListenerSupport.create(BeanChangedListener.class);

  // Listener registration methods
  //--------------------------------------------------

  protected Registration addBeanChangedListener(final BeanChangedListener<T, SUB> listener) {
    beanChangedListeners.addListener(listener);

    return (() -> removeBeanChangedListener(listener));
  }

  protected void removeBeanChangedListener(final BeanChangedListener<T, SUB> listener) {
    beanChangedListeners.removeListener(listener);
  }

  // Listener dispatch methods
  //--------------------------------------------------

  protected void fireBeanChangedEvent(final T newBean, final T oldBean) {
    beanChangedListeners.fire().beanChanged(new BeanChangedEvent<>(newBean, oldBean, (SUB)this));
  }

  // Getters/setters
  //--------------------------------------------------

  protected T getBean() {
    return bean;
  }

  protected void setBean(final T bean) {
    final T oldBean = this.bean;

    this.bean = bean;

    if(bean != oldBean) {
      fireBeanChangedEvent(bean, oldBean);
    }
  }

  // Object methods
  //--------------------------------------------------

  @Override
  public boolean equals(final Object other) {
    if(this == other) return true;

    if(getClass().isAssignableFrom(other.getClass())) {
      return getBean().equals(((ObservableBean<?, ?>)other).getBean());
    } else if(getBean().getClass().isAssignableFrom(other.getClass())) {
      return getBean().equals(other);
    }

    return false;
  }

  @Override
  public int hashCode() {
    return getBean().hashCode();
  }

  @Override
  public String toString() {
    return getBean().toString();
  }

}
